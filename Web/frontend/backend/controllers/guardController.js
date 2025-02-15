const Guard = require("../models/Guard");


const createGuard = async (req, res) => {
  try {
    const { name, age, contact, address, pastWorkHistory, currentDeployment, location } = req.body;


    const defaultLocation = {
      type: "Point",
      coordinates: [0, 0] 
    };

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




const getAllGuards = async (req, res) => {
  try {
    const guards = await Guard.find();
    res.json({ guards });
  } catch (error) {
    res.status(500).json({ message: "Error fetching guards", error: error.message });
  }
};

const getGuardById = async (req, res) => {
  try {
    const guard = await Guard.findById(req.params.id);
    if (!guard) return res.status(404).json({ message: "Guard not found" });

    res.json({ guard });
  } catch (error) {
    res.status(500).json({ message: "Error fetching guard details", error: error.message });
  }
};


const updateGuard = async (req, res) => {
  try {
    const guard = await Guard.findByIdAndUpdate(req.params.id, req.body, { new: true });

    if (!guard) return res.status(404).json({ message: "Guard not found" });

    res.json({ message: "Guard updated successfully", guard });
  } catch (error) {
    res.status(500).json({ message: "Error updating guard", error: error.message });
  }
};

const deleteGuard = async (req, res) => {
  try {
    const guard = await Guard.findByIdAndDelete(req.params.id);
    if (!guard) return res.status(404).json({ message: "Guard not found" });

    res.json({ message: "Guard deleted successfully" });
  } catch (error) {
    res.status(500).json({ message: "Error deleting guard", error: error.message });
  }
};


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