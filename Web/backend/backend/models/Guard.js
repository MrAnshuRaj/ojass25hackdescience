const mongoose = require("mongoose");

const GuardSchema = new mongoose.Schema({
  name: {
    type: String,
    required: [true, 'Name is required'],
    trim: true
  },
  age: {
    type: Number,
    required: [true, 'Age is required'],
    min: [18, 'Guard must be at least 18 years old']
  },
  contact: {
    type: String,
    required: [true, 'Contact number is required'],
    match: [/^\d{10}$/, 'Please enter a valid 10-digit contact number']
  },
  address: {
    type: String,
    required: [true, 'Address is required'],
    trim: true
  },
  pastWorkHistory: {
    type: String,
    trim: true,
    default: 'No previous work history'
  },
  currentDeployment: {
    type: String,
    default: 'Unassigned'
  },
  isOnDuty: { 
    type: Boolean, 
    default: false 
  },
  location: {
    type: {
      type: String,
      enum: ['Point'],
      default: 'Point',
      required: true
    },
    coordinates: {
      type: [Number],
      
      validate: {
        validator: function(v) {
          return v.length === 2 && 
                 v[0] >= -180 && v[0] <= 180 && 
                 v[1] >= -90 && v[1] <= 90;
        },
        message: 'Invalid coordinates. Must be [longitude, latitude]'
      }
    }
  }
}, { 
  timestamps: true 
});

// Add geospatial index for location queries
GuardSchema.index({ location: "2dsphere" });

// Add methods if needed, similar to User model
GuardSchema.methods.updateLocation = async function(longitude, latitude) {
  this.location.coordinates = [longitude, latitude];
  return await this.save();
};

// Using CommonJS exports instead of ES modules
module.exports = mongoose.model("Guard", GuardSchema);