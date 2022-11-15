import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from 'axios';
import {
  Container,
  Button,
  Row,
  Col,
  Form,
  FormControl
} from "react-bootstrap";

class Signup extends Component {
  constructor(props) {
    super(props);
    this.state = {
      firstName: "",
      lastName: "",
      password: "",
      jmbg: "",
      email: "",
      status: true
    };
    this.error_custom = "";
  }
  onChange = e => {
    this.setState({ [e.target.name]: e.target.value });
  };

  onSignupClick = async () => {
    const userData = {
      firstName: this.state.firstName,
      lastName: this.state.lastName,
      password: this.state.password,
      jmbg: this.state.jmbg,
      email: this.state.email,
      status: true
    };
    console.log(userData)
    await axios.post('http://localhost:8084/api/user',userData)
        .catch(function (error) {
          this.error_custom = "~";
          console.log("Error custom! ~" + this.error_custom)
          if (error.response) {
            console.log(error.response.data);
            console.log(error.response.status);
            console.log(error.response.headers);
          }
          else if (error.request) {
            console.log(error.request);
          } else {
            console.log('Error', error.message);
          }
          console.log(error.config);
        });
    //const response = await axios.get('http://localhost:8084/api/user/1');
    //console.log(response)
    //this.setState({ totalReactPackages: response.data.total })
  };

  render() {
    return (
      <Container>
        <Row>
          <Col md="4">
            <h1>Sign up</h1>
            <Form>
              <Form.Group controlId="firstNameId">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  type="text"
                  name="firstName"
                  placeholder="Enter name"
                  value={this.state.firstName}
                  onChange={this.onChange}
                />
                <FormControl.Feedback type="invalid"></FormControl.Feedback>
              </Form.Group>
              <Form.Group controlId="lastNameId">
                <Form.Label>Surname</Form.Label>
                <Form.Control
                  type="text"
                  name="lastName"
                  placeholder="Enter lastName"
                  value={this.state.lastName}
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
                  value={this.password}
                  onChange={this.onChange}
                />
                <Form.Control.Feedback type="invalid"></Form.Control.Feedback>
              </Form.Group>
              <Form.Group controlId="emailId">
                <Form.Label>Your home email</Form.Label>
                <Form.Control
                  type="email"
                  name="email"
                  placeholder="Enter your email"
                  value={this.email}
                  onChange={this.onChange}
                />
                <Form.Control.Feedback type="invalid"></Form.Control.Feedback>
              </Form.Group>
              <Form.Group controlId="jmbgId">
                <Form.Label>JMBG </Form.Label>
                <Form.Control
                  type="number"
                  name="jmbg"
                  placeholder="Enter jmbg"
                  value={this.jmbg}
                  onChange={this.onChange}
                />
                <Form.Control.Feedback type="invalid"></Form.Control.Feedback>
              </Form.Group>
            </Form>
            <Button 
              color="primary"
              onClick={this.onSignupClick}  
            >Sign up</Button>
            <p className="mt-2">
              Already have account? <Link to="/login">Login</Link>
            </p>
          </Col>
        </Row>
      </Container>
    );
  }
}

export default Signup;