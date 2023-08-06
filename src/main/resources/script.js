// Function to get the current date and time
function getCurrentDateTime() {
  const now = new Date();
  const options = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    second: 'numeric',
    hour12: false
  };
  return now.toLocaleString(undefined, options);
}

// Function to update the current date and time in the HTML template
function updateCurrentDateTime() {
  const currentDateTime = getCurrentDateTime();
  const dateTimeElement = document.getElementById('currentDateTime');
  if (dateTimeElement) {
    dateTimeElement.textContent = currentDateTime;
  }
}

// Update the current date and time when the page is loaded
document.addEventListener('DOMContentLoaded', updateCurrentDateTime);

// Update the current date and time every 5 seconds (for a dynamic display)
setInterval(updateCurrentDateTime, 5000); // update every 5 seconds
