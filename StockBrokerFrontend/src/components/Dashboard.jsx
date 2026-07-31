import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SockJS from "sockjs-client";
import { over } from "stompjs";

const Dashboard = () => {
  const navigate = useNavigate();
  const [stocks, setStocks] = useState([]);
  const [myStocks, setMyStocks] = useState([]);
  const [message, setMessage] = useState("");

  const token = localStorage.getItem("token");
  const email = localStorage.getItem("email");

  // WebSocket client
  let stompClient = null;

  useEffect(() => {
    if (!token) {
      navigate("/");
    } else {
      fetchAllStocks();
      fetchMyStocks();
      connectWebSocket();
    }
  }, []);

  // ✅ Connect to backend WebSocket
  const connectWebSocket = () => {
  const socket = new SockJS("http://localhost:8080/ws");
  stompClient = over(socket);

  stompClient.connect({}, () => {
    console.log("✅ WebSocket connected");

    stompClient.subscribe("/topic/stocks", (response) => {
      const updatedStocks = JSON.parse(response.body);
      setStocks(updatedStocks);

      // 🔁 Update myStocks with new prices
      setMyStocks((prevMyStocks) =>
        prevMyStocks.map((stock) => {
          const updated = updatedStocks.find(
            (s) => s.ticker === stock.ticker
          );
          return updated ? { ...stock, price: updated.price } : stock;
        })
      );
    });
  });
};


  // ✅ Fetch all stocks (initial load)
  const fetchAllStocks = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/stocks/all");
      const data = await res.json();
      setStocks(data);
    } catch (err) {
      console.error("Error fetching stocks:", err);
    }
  };

  // ✅ Fetch user’s subscribed stocks
  const fetchMyStocks = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/stocks/my", {
        headers: { Authorization: `Bearer ${token}` },
      });
      const data = await res.json();
      setMyStocks(data);
    } catch (err) {
      console.error("Error fetching subscribed stocks:", err);
    }
  };

  // ✅ Subscribe / Unsubscribe logic
  const handleSubscribe = async (ticker) => {
    const res = await fetch(`http://localhost:8080/api/stocks/subscribe/${ticker}`, {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
    });
    const data = await res.json();
    setMessage(data.message);
    fetchMyStocks();
  };

  const handleUnsubscribe = async (ticker) => {
    const res = await fetch(`http://localhost:8080/api/stocks/unsubscribe/${ticker}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    });
    const data = await res.json();
    setMessage(data.message);
    fetchMyStocks();
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  const isSubscribed = (ticker) =>
    myStocks.some((stock) => stock.ticker === ticker);

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      {/* Header */}
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">
          Welcome, <span className="text-blue-400">{email}</span>
        </h1>
        <button
          onClick={handleLogout}
          className="bg-red-600 hover:bg-red-700 px-4 py-2 rounded-lg font-semibold"
        >
          Logout
        </button>
      </div>

      {/* Status Message */}
      {message && (
        <p className="text-green-400 text-center mb-4 font-medium">{message}</p>
      )}

      {/* All Stocks */}
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {stocks.map((stock) => (
          <div
            key={stock.id}
            className="bg-gray-800 p-5 rounded-2xl shadow-lg flex flex-col justify-between"
          >
            <div>
              <h2 className="text-xl font-bold">{stock.ticker}</h2>
              <p className="text-gray-300 mt-1">
                Current Price:{" "}
                <span className="text-green-400 font-semibold">
                  ${stock.price?.toFixed(2)}
                </span>
              </p>
            </div>
            {isSubscribed(stock.ticker) ? (
              <button
                onClick={() => handleUnsubscribe(stock.ticker)}
                className="mt-4 bg-red-600 hover:bg-red-700 py-2 rounded-lg font-semibold"
              >
                Unsubscribe
              </button>
            ) : (
              <button
                onClick={() => handleSubscribe(stock.ticker)}
                className="mt-4 bg-blue-600 hover:bg-blue-700 py-2 rounded-lg font-semibold"
              >
                Subscribe
              </button>
            )}
          </div>
        ))}
      </div>

      {/* My Stocks Section */}
      <div className="mt-10">
        <h2 className="text-2xl font-bold mb-3">My Subscribed Stocks</h2>
        {myStocks.length > 0 ? (
          <ul className="space-y-2">
            {myStocks.map((stock) => (
              <li
                key={stock.id}
                className="bg-gray-800 p-3 rounded-lg flex justify-between"
              >
                <span className="font-semibold">{stock.ticker}</span>
                <span className="text-green-400">${stock.price?.toFixed(2)}</span>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-gray-400">You haven’t subscribed to any stocks yet.</p>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
