import React, {useState}  from 'react' 
import {
    Container,
    Button,
    Row,
    Col,
    Form,
    FormControl
  } from "react-bootstrap";
import axios from 'axios';
import { Link, Navigate, Route, Routes, useNavigate } from "react-router-dom";


export default function LoginFunc(){
    const navigate = useNavigate();
    const [state,setStates] = useState({email: "", password:""});

    const onLoginClick = async () => {
        const userData = {
          email: state.email,
          password:  state.password
        };
        console.log("Login " + userData.email + " " + userData.password);
        console.log(userData);
    
        const options = {
          method: 'POST',
          url: 'http://localhost:8084/api/user/login',
          headers: {'Content-Type': 'application/json'},
          data: {email: userData.email, password: userData.password}
        };
        await axios.request(options).then(function (response) {
          console.log(response.data);
          console.log("Succesfully logged in!");
    
          //return <Link to="/"></Link>;
          //return <Navigate to="/nista"/>;
          navigate("/");
    
        }).catch(function (error) {
          console.error(error);
          console.log("Invalid log in info!");
        });
      };
      return (
        <Container>
        <Row>
          <Col md="4">
            <h1>Login</h1>
            <Form>
              <Form.Group controlId="emailId">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  type="email"
                  name="email"
                  placeholder="Enter email"
                  value={state.email}
                  //onChange={onChangeFunc}
                  onChange={e=> setStates({...state, email: e.target.value})}
                />
                <FormControl.Feedback type="invalid"></FormControl.Feedback>
              </Form.Group>

              <Form.Group controlId="passwordId">
                <Form.Label>Your password</Form.Label>
                <Form.Control
                  type="password"
                  name="password"
                  placeholder="Enter password"
                  value={state.password}
                  //onChange={onChangeFunc}
                  onChange={e=> setStates({...state, password: e.target.value})}
                />
                <Form.Control.Feedback type="invalid"></Form.Control.Feedback>
              </Form.Group>
            </Form>
            <Button color="primary" onClick={onLoginClick}>
               Login
            </Button>
            <p className="mt-2">
              Don't have account? <Link to="/signup">Signup</Link>
            </p>
          </Col>
        </Row>

    </Container>
      )
}