const Guard = require("../models/Guard");

// @desc   Create a new security guard
// @route  POST /api/guards
// @access Admin
const createGuard = async (req, res) => {
  try {
    const { name, age, contact, address, pastWorkHistory, currentDeployment, location } = req.body;

    // ✅ Default location if not provided
    const defaultLocation = {
      type: "Point",
      coordinates: [0, 0] // Default to (0,0) if missing
    };

    // ✅ Ensure location exists and has coordinates
    const guard = new Guard({ 
      name, 
      age: Number(age), 
      contact, 
      address, 
      pastWorkHistory, 
      currentDeployment, 
      location: location && location.coordinates ? location : defaultLocation
    });

    await guard.save();
    res.status(201).json({ message: "Guard created successfully", guard });

  } catch (error) {
    console.error("Error creating guard:", error);
    res.status(500).json({ message: "Error creating guard", error: error.message });
  }
};



// @desc   Get all guards
// @route  GET /api/guards
// @access Admin, Police, Society Owners
const getAllGuards = async (req, res) => {
  try {
    const guards = await Guard.find();
    res.json({ guards });
  } catch (error) {
    res.status(500).json({ message: "Error fetching guards", error: error.message });
  }
};

// @desc   Get a specific guard by ID
// @route  GET /api/guards/:id
// @access Admin, Police, Society Owners
const getGuardById = async (req, res) => {
  try {
    const guard = await Guard.findById(req.params.id);
    if (!guard) return res.status(404).json({ message: "Guard not found" });

    res.json({ guard });
  } catch (error) {
    res.status(500).json({ message: "Error fetching guard details", error: error.message });
  }
};

// @desc   Update guard details
// @route  PUT /api/guards/:id
// @access Admin
const updateGuard = async (req, res) => {
  try {
    const guard = await Guard.findByIdAndUpdate(req.params.id, req.body, { new: true });

    if (!guard) return res.status(404).json({ message: "Guard not found" });

    res.json({ message: "Guard updated successfully", guard });
  } catch (error) {
    res.status(500).json({ message: "Error updating guard", error: error.message });
  }
};

// @desc   Delete a guard
// @route  DELETE /api/guards/:id
// @access Admin
const deleteGuard = async (req, res) => {
  try {
    const guard = await Guard.findByIdAndDelete(req.params.id);
    if (!guard) return res.status(404).json({ message: "Guard not found" });

    res.json({ message: "Guard deleted successfully" });
  } catch (error) {
    res.status(500).json({ message: "Error deleting guard", error: error.message });
  }
};

// @desc   Update guard location (real-time tracking)
// @route  POST /api/guards/update-location
// @access Guard (Authenticated)
const updateGuardLocation = async (req, res) => {
  try {
    const { guardId, longitude, latitude } = req.body;

    const guard = await Guard.findByIdAndUpdate(
      guardId,
      { location: { type: "Point", coordinates: [longitude, latitude] } },
      { new: true }
    );

    if (!guard) return res.status(404).json({ message: "Guard not found" });

    res.json({ message: "Location updated", location: guard.location });
  } catch (error) {
    res.status(500).json({ message: "Error updating location", error: error.message });
  }
};

// @desc   Toggle guard's on-duty/off-duty status
// @route  PATCH /api/guards/:id/toggle-duty
// @access Guard (Authenticated)
const toggleDutyStatus = async (req, res) => {
  try {
    const guard = await Guard.findById(req.params.id);

    if (!guard) return res.status(404).json({ message: "Guard not found" });

    guard.isOnDuty = !guard.isOnDuty;
    await guard.save();

    res.json({ messages: `Guard is now ${guard.isOnDuty ? "on-duty" : "off-duty"}`, guard });
  } catch (error) {
    res.status(500).json({ message: "Error updating duty status", error: error.message });
  }
};

module.exports = { createGuard, getAllGuards, getGuardById, updateGuard, deleteGuard, updateGuardLocation, toggleDutyStatus };