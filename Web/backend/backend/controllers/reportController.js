const Report = require("../models/Report");

/**
 * @desc Create a new report
 * @route POST /api/reports
 * @access Private (User must be authenticated)
 */
const createReport = async (req, res) => {
    try {
        const { guard, description } = req.body;
        const reportedBy = req.user.id; // Extracted from auth middleware

        if (!guard || !description) {
            return res.status(400).json({ error: "Guard and description are required" });
        }

        let report = new Report({
            reportedBy,
            guard,
            description,
        });

        await report.save();

        report = await Report.findById(report._id)
            .populate("reportedBy", "name email")
            .populate("guard", "name badgeNumber");

        res.status(201).json({ message: "Report submitted successfully", report });
    } catch (error) {
        console.error("Error creating report:", error);
        res.status(500).json({ error: "Internal server error" });
    }
};

/**
 * @desc Get all reports (Admin Only)
 * @route GET /api/reports
 * @access Private (Admin only)
 */
const getAllReports = async (req, res) => {
    try {
        const reports = await Report.find()
            .populate("reportedBy", "name email")
            .populate("guard", "name badgeNumber");

        res.status(200).json({ reports });
    } catch (error) {
        console.error("Error fetching reports:", error);
        res.status(500).json({ error: "Internal server error" });
    }
};

/**
 * @desc Get a single report by ID
 * @route GET /api/reports/:id
 * @access Private (Admin only)
 */
const getReportById = async (req, res) => {
    try {
        const report = await Report.findById(req.params.id)
            .populate("reportedBy", "name email")
            .populate("guard", "name badgeNumber");

        if (!report) {
            return res.status(404).json({ error: "Report not found" });
        }

        res.status(200).json({ report });
    } catch (error) {
        console.error("Error fetching report:", error);
        res.status(500).json({ error: "Internal server error" });
    }
};

/**
 * @desc Delete a report (Admin Only)
 * @route DELETE /api/reports/:id
 * @access Private (Admin only)
 */
const deleteReport = async (req, res) => {
    try {
        const report = await Report.findById(req.params.id)
            .populate("reportedBy", "name email")
            .populate("guard", "name badgeNumber");

        if (!report) {
            return res.status(404).json({ error: "Report not found" });
        }

        await report.deleteOne();
        res.status(200).json({ message: "Report deleted successfully", report });
    } catch (error) {
        console.error("Error deleting report:", error);
        res.status(500).json({ error: "Internal server error" });
    }
};

/**
 * @desc Resolve a report (Admin Only)
 * @route PATCH /api/reports/:id/resolve
 * @access Private (Admin only)
 */
const resolveReport = async (req, res) => {
    try {
        const report = await Report.findById(req.params.id)
            .populate("reportedBy", "name email")
            .populate("guard", "name badgeNumber");

        if (!report) {
            return res.status(404).json({ error: "Report not found" });
        }

        report.status = "Resolved";
        await report.save();

        res.status(200).json({ message: "Report resolved successfully", report });
    } catch (error) {
        console.error("Error resolving report:", error);
        res.status(500).json({ error: "Internal server error" });
    }
};

module.exports = {
    createReport,
    getAllReports,
    getReportById,
    deleteReport,
    resolveReport,
};