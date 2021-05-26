import React, { Component } from "react";
import { useHistory, withRouter, Link } from "react-router-dom";
import './home.css';
import Nav from '../User/nav.js';
import Form from 'react-bootstrap/Form';
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row'
import { ThemeConsumer } from "react-bootstrap/esm/ThemeProvider";


const validEmailRegex = RegExp(/^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i);
const validMobileRegex = RegExp(/^[6-9]{1}[0-9]{9}$/);


class ViewProfile extends Component {

  constructor(props) {
    super(props)
    this.state = {
      id: this.props.match.params.id,
      profile: []
    };
  }


  async componentDidMount() {
    const url = "http://localhost:9000/viewProfile/" + this.state.id;
    let headers = new Headers();

    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');

    headers.append('Access-Control-Allow-origin', url);
    headers.append('Access-Control-Allow-Credentials', 'true');

    headers.append('POST', 'GET');

    await fetch(url, {
      headers: headers,
      method: 'GET',
    })
      .then(response => response.json())
      .then(response => {
        this.setState({ profile: response })
      });
  }

  renderList(farmer) {
    const { profile } = this.state
    return (
      <div className="mb-3">
        <div className="auth-inner">
          Name: {profile.name} <br />
        Email: {profile.email} <br />
        Mobile: {profile.mobile} <br />
        Rating: {profile.rating} <br />
        Role: {profile.role}
        </div>
      </div>
    )
  }



  render() {
    var uid = this.props.match.params.id
    var role = this.props.match.params.role

    return (

        <div>

          <Nav uid={uid} />
          <br />
                <br />
                <br />

          {/* <center><h3>View Profile</h3></center><br /> */}
          {this.renderList(this.state.id)}
          <br />
        </div>
    );
  }
}


export default ViewProfile;