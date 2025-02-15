const Report = require("../models/Report");


const createReport = async (req, res) => {
    try {
        const { guard, description } = req.body;
        const reportedBy = req.user.id;

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