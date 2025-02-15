const express = require("express");
const {createGuard, getAllGuards, getGuardById, updateGuard, deleteGuard, updateGuardLocation ,toggleDutyStatus  } = require("../controllers/guardController");

const router = express.Router();


router.post("/", createGuard);


router.get("/", getAllGuards);

router.get("/:id", getGuardById);


router.put("/:id", updateGuard);


router.delete("/:id", deleteGuard);


router.post("/update-location", updateGuardLocation);

router.patch("/:id/toggle-duty", toggleDutyStatus);

module.exports = router;
