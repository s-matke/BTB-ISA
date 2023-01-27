import React, {useState, useEffect}  from 'react' 
import { useNavigate } from "react-router-dom";
import axios from 'axios';


function BookAppointment(){
  const navigate = useNavigate();
  const [userInfo,setUserInfo] = useState([]);
  const [selectedItem,setSelectedItem] = useState([]);
  const [answers,setAnswers] = useState([]);
  const [userLoaded, setUserLoaded] = useState(false);
  const [isAppointmentBooked, setIsappointmentBooked] = useState(true);
  const [appointmentBooked, setAppointmentBooked] = useState([]);

  const fetchData = async () => {
        const options = {
        method: 'GET',
        url: 'http://localhost:8084/api/appointment/getAllAvailable',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
        };

        axios.request(options).then(function (response) {
        console.log(response.data);
        setAnswers(response.data);
        setUserLoaded(true)

        }).catch(function (error) {
        console.error(error);
        return { success: false };
        });
    }
    useEffect(() => {
      const checkBook = async () => {
        const options = {
          method: 'GET',
          url: 'http://localhost:8084/api/appointment/getBooked/'+JSON.parse(localStorage.getItem('user')).id.toString(),
          headers: {
            Authorization: 'Bearer ' + localStorage.getItem('token')
          }
        };
        console.log("UserID: " + JSON.parse(localStorage.getItem('user')).id.toString());
        axios.request(options).then(function (response) {
          // if (response.status === 500) {
          //   setIsAppointmentBooked(false);
          //   setAppointmentBooked([]);
          // }
          //setUserLoaded(false);
          //setAppointmentBooked(response.data);
          console.log("Odgovor:", response.data);
          setAppointmentBooked(response.data);
          console.log("Appointment booked and response data:", appointmentBooked, response.data);
          setIsappointmentBooked(false)
          
          return response.data;
        }).catch(function (error) {
          //Ako nema zakazanih termina kod ovog korisnika..
          setIsappointmentBooked(true)
          return [];
        });
  }
      checkBook();

      console.log("appointmenet Booked: ", appointmentBooked);
      //console.log("Tu sam!", appointmentBooked, isAppointmentBooked);
      if (appointmentBooked.length === 0) {
        fetchData();
      }
     
      
      }, []);

      const handleSave = () => {
        if (selectedItem) {
          // Do something with the selected item, for example, send it to an API
          console.log("Selected item:", selectedItem);

          const loggedInUser = JSON.parse(localStorage.getItem('user'));
          setUserInfo(loggedInUser.id.toString());
          console.log("User:",  userInfo.toString());
          const options = {
            method: 'PUT',
            url: 'http://localhost:8084/api/appointment/book/'+ selectedItem.toString() +'/' + userInfo.toString() + '/',
            headers: {
              Authorization: 'Bearer ' + localStorage.getItem('token')
            }
          };
          axios.request(options).then(function (response) {
            console.log(response.data);
            navigate("/centers")
          }).catch(function (error) {
            console.error(error);
            navigate("/bookAppointments")
          });
        } else {
          console.log("No item selected");
        }
      };

      const handleCancle = () => {
        console.log(appointmentBooked.id);
        const options = {
          method: 'POST',
          url: 'http://localhost:8084/api/appointment/cancel',
          headers: {
            Authorization: 'Bearer ',
            'Content-Type': 'application/json'
          },
          data: {appointmentId: appointmentBooked.id, customerId: JSON.parse(localStorage.getItem('user')).id.toString()}
        };
        
        axios.request(options).then(function (response) {
          console.log(response.data);
          console.log("Otkazao!");
          navigate("/centers")
        }).catch(function (error) {
          console.error(error);
          console.log("Eror prilikom otkazivanja")
        });
      };

      return (
    <div style={{'marginLeft':'280px'}}>
    {isAppointmentBooked ? (
       <div className="Neso">
         <h1>Lista slobodnih termina</h1>
         {answers.map((element, index)=>
         (
          <div className={index+1}>
            <p key={index+1}>{element.date  + "\t\t" + element.time}</p>
            <label>
            <input
              type="radio"
              name="same"
              value={element.id}
              key={index+1}
              onChange={() => setSelectedItem(element.id)}
              
            />
            Izaberi
          </label>
          </div>     
          )
         )
        }
         <button onClick={handleSave}>Rezervisi</button>
       </div>
    ) : (
      <div>
        <p>Vec imate zakazan termin</p>
        {appointmentBooked && <div>{appointmentBooked.data}</div>}
        <button onClick={handleCancle}>Otkazi</button>
      </div>
    )}  
    </div>
      )
}
export default BookAppointment;