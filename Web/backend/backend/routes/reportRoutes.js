const express = require("express");
const router = express.Router();
const {
  createReport,
  getAllReports,
  getReportById,
  deleteReport,
  resolveReport,
} = require("../controllers/reportController");

const authMiddleware  = require("../middleware/authMiddleware"); // Import middleware

// 🔹 Create a new report (Protected)
router.post("/", authMiddleware, createReport);

// 🔹 Get all reports (Protected)
router.get("/", authMiddleware, getAllReports);

// 🔹 Get a single report by ID (Protected)
router.get("/:id", authMiddleware, getReportById);

// 🔹 Delete a report (Protected)
router.delete("/:id", authMiddleware, deleteReport);

// 🔹 Resolve a report (Protected)
router.patch("/:id/resolve", authMiddleware, resolveReport);

module.exports = router;
 