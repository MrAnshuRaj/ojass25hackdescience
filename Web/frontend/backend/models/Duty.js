const mongoose = require("mongoose");

const DutySchema = new mongoose.Schema({
  guard: { type: mongoose.Schema.Types.ObjectId, ref: "Guard" },
  assignedBy: { type: mongoose.Schema.Types.ObjectId, ref: "User" },
  location: String,
  shiftStart: Date,
  shiftEnd: Date,
  status: { type: String, enum: ["Assigned", "Completed", "Ongoing"], default: "Assigned" }
}, { timestamps: true });

module.exports = mongoose.model("Duty", DutySchema);
