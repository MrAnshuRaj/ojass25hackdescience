const express = require("express");
const dotenv = require("dotenv");
const cors = require("cors");
const connectDB = require("./config/db");
const bodyParser = require("body-parser");


const authRoutes = require("./routes/authRoutes");
const guardRoutes = require("./routes/guardRoutes");
const dutyRoutes = require("./routes/dutyRoutes");
const reportRoutes = require("./routes/reportRoutes")

dotenv.config();
connectDB();

const app = express();
app.use(express.json({ strict: false })); 
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true}));

app.use("/api/v1/auth", authRoutes);
app.use("/api/v1/guards",guardRoutes);
app.use("/api/v1/duty", dutyRoutes);
app.use("/api/v1/report" , reportRoutes);


const PORT = process.env.PORT || 5000;
const server = app.listen(PORT, () => console.log(`Server running on port ${PORT}`));

// const io = require("socket.io")(server);
// require("./socket/tracking")(io);
