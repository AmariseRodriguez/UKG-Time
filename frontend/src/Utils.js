class Utils {
    getPunchList = async (employeeId) => {
        try {
            // Ensure the URL includes the protocol (http://)
            const response = await fetch(`http://localhost:8080/api/user/viewRecentPunchList?id=${employeeId}`);

            // Check if the response is ok (status code 200-299)
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            // Parse the response body as JSON

            const jsonResponse = await response.json();
            // console.log(jsonResponse)

            const dateHash = {}
            jsonResponse.forEach(punch => {
                const date = punch.dateTime.split(" ")[0]; // Extract the date part
                if (!dateHash[date]) {
                    dateHash[date] = [];
                }
                dateHash[date].push(punch);
            });   
            // console.log(dateHash)

            return dateHash;
        } catch (error) {
            // Log any errors that occur during the fetch or JSON parsing
            console.error('There was an error!', error);
        }
    };
}
const utils = new Utils();
export default utils;