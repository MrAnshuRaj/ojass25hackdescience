const mongoose = require("mongoose");

const ReportSchema = new mongoose.Schema({
  reportedBy: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true },
  guard: { type: mongoose.Schema.Types.ObjectId, ref: "Guard", required: true },
  description: { type: String, required: true },
  status: { type: String, enum: ["Pending", "Resolved"], default: "Pending" }
}, { timestamps: true });

module.exports = mongoose.model("Report", ReportSchema);