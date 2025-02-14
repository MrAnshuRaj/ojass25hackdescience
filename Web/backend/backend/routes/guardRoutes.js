const express = require("express");
const {createGuard, getAllGuards, getGuardById, updateGuard, deleteGuard, updateGuardLocation ,toggleDutyStatus  } = require("../controllers/guardController");

const router = express.Router();

// @route  POST /api/guards
// @desc   Create a new guard (Admin only)
router.post("/", createGuard);

// @route  GET /api/guards
// @desc   Get all guards (Admin, Police, Society Owners)
router.get("/", getAllGuards);

// @route  GET /api/guards/:id
// @desc   Get a specific guard by ID (Admin, Police, Society Owners)
router.get("/:id", getGuardById);

// @route  PUT /api/guards/:id
// @desc   Update a guard's details (Admin only)
router.put("/:id", updateGuard);

// @route  DELETE /api/guards/:id
// @desc   Delete a guard (Admin only)
router.delete("/:id", deleteGuard);

// @route  POST /api/guards/update-location
// @desc   Update guard's real-time location (Guard only)
router.post("/update-location", updateGuardLocation);

// @route  PATCH /api/guards/:id/toggle-duty
// @desc   Toggle guard's duty status (Guard only)
router.patch("/:id/toggle-duty", toggleDutyStatus);

module.exports = router;
