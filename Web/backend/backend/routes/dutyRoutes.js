const express = require("express");
const { getAllDuties, getDutyById, getGuardDuties, assignDuty, updateDuty, deleteDuty } = require("../controllers/dutyController");
const authMiddleware = require("../middleware/authMiddleware");

const router = express.Router();

router.get("/", authMiddleware, getAllDuties);
router.get("/:id", authMiddleware, getDutyById);
router.get("/guard/:guardId", authMiddleware, getGuardDuties);
router.post("/", authMiddleware, assignDuty);
router.put("/:id", authMiddleware, updateDuty);
router.delete("/:id", authMiddleware, deleteDuty);

module.exports = router;
