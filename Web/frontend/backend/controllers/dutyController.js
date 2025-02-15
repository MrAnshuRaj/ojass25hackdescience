const Duty = require("../models/Duty");
const Guard = require("../models/Guard");


const getAllDuties = async (req, res) => {
  try {
    const duties = await Duty.find()
      .populate('assignedBy', 'name') 
      .populate('guard', 'name location status') 
      .lean()  
      .exec();

    res.status(200).json(duties);
  } catch (error) {
    res.status(500).json({ 
      message: "Error fetching duties", 
      error: error.message 
    });
  }
}; 


const getDutyById = async (req, res) => {
  try {
    const duty = await Duty.findById(req.params.id).populate("guard", "name location status");
    if (!duty) return res.status(404).json({ message: "Duty not found" });

    res.status(200).json(duty);
  } catch (error) {
    res.status(500).json({ message: "Error fetching duty", error: error.message });
  }
};


const getGuardDuties = async (req, res) => {
  try {
    console.log("Guard ID:", req.params.guardId);
    const duties = await Duty.find({ guard: req.params.guardId }).populate("guard", "name location status");
    if (duties.length === 0) return res.status(404).json({ message: "No duties found for this guard" });

    res.status(200).json(duties);
  } catch (error) {
    res.status(500).json({ message: "Error fetching guard duties", error: error.message });
  }
};




const assignDuty = async (req, res) => {
  try {
    const { guardId, location, shiftStart, shiftEnd } = req.body;

 
    if (!req.user || !req.user.id) {
      return res.status(401).json({ message: "Unauthorized: Admin required" });
    }


    const guard = await Guard.findById(guardId);
    if (!guard) {
      return res.status(404).json({ message: "Guard not found" });
    }

    const duty = new Duty({
      guard: guardId, 
      assignedBy: req.user.id, 
      location,
      shiftStart: new Date(shiftStart),
      shiftEnd: new Date(shiftEnd), 
    });

    console.log("New Duty Object Before Save:", duty);
     await duty.save();
    console.log("Duty successfully saved.");

    res.status(201).json({ message: "Duty assigned successfully", duty: duty });

  } catch (error) {
    console.error("Error assigning duty:", error);
    res.status(400).json({ message: "Error assigning duty", error: error.message });
  }
};



const updateDuty = async (req, res) => {
  try {
    const updatedDuty = await Duty.findByIdAndUpdate(req.params.id, req.body, { new: true });
    if (!updatedDuty) return res.status(404).json({ message: "Duty not found" });

    res.status(200).json({ message: "Duty updated", duty: updatedDuty });
  } catch (error) {
    res.status(400).json({ message: "Error updating duty", error: error.message });
  }
};


const deleteDuty = async (req, res) => {
  try {
    const deletedDuty = await Duty.findByIdAndDelete(req.params.id);
    if (!deletedDuty) return res.status(404).json({ message: "Duty not found" });

    res.status(200).json({ message: "Duty deleted successfully" });
  } catch (error) {
    res.status(500).json({ message: "Error deleting duty", error: error.message });
  }
};

module.exports = { getAllDuties, getDutyById, getGuardDuties, assignDuty, updateDuty, deleteDuty };
