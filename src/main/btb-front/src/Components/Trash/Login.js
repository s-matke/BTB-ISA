import React, { Component } from "react";
import { Link, Navigate } from "react-router-dom";
import axios from 'axios';
import {
  Container,
  Button,
  Row,
  Col,
  Form,
  FormControl
} from "react-bootstrap";


class Login extends Component {
  
  constructor(props) {
    super(props);
    this.state = {
      email: "",
      password: ""
    };
  }
  onChange = e => {
    this.setState({ [e.target.name]: e.target.value });
  };

  onLoginClick = async () => {
    const userData = {
      email: this.state.email,
      password:  this.state.password
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

    }).catch(function (error) {
      console.error(error);
      console.log("Invalid log in info!");
    });
  };
  render() {
    
    return (
      <Container>
        <Row>
          <Col md="4">
            <h1>Login</h1>
            <Form>
              <Form.Group controlId="emailId">
                <Form.Label>User name</Form.Label>
                <Form.Control
                  type="email"
                  name="email"
                  placeholder="Enter email"
                  value={this.state.username}
                  onChange={this.onChange}
                />
                <FormControl.Feedback type="invalid"></FormControl.Feedback>
              </Form.Group>

              <Form.Group controlId="passwordId">
                <Form.Label>Your password</Form.Label>
                <Form.Control
                  type="password"
                  name="password"
                  placeholder="Enter password"
                  value={this.state.password}
                  onChange={this.onChange}
                />
                <Form.Control.Feedback type="invalid"></Form.Control.Feedback>
              </Form.Group>
            </Form>
            <Button color="primary" onClick={this.onLoginClick} > Login
            </Button>
            <p className="mt-2">
              Don't have account? <Link to="/signup">Signup</Link>
            </p>
          </Col>
        </Row>
      </Container>
    );
  }
}

export default Login;
