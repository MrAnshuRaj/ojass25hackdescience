const express = require("express");
const router = express.Router();
const {
  createReport,
  getAllReports,
  getReportById,
  deleteReport,
  resolveReport,
} = require("../controllers/reportController");

const authMiddleware  = require("../middleware/authMiddleware"); 


router.post("/", authMiddleware, createReport);


router.get("/", authMiddleware, getAllReports);


router.get("/:id", authMiddleware, getReportById);


router.delete("/:id", authMiddleware, deleteReport);


router.patch("/:id/resolve", authMiddleware, resolveReport);

module.exports = router;
 