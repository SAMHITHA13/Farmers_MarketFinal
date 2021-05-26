import React, { Component } from "react";
import { useHistory, withRouter, Link } from "react-router-dom";
import './home.css';
import Nav from '../User/nav.js';
import Form from 'react-bootstrap/Form';
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row'

var a = window.localStorage.getItem("name")
var b = window.localStorage.getItem("email")
var c = window.localStorage.getItem("mobile")
//var d=window.localStorage.getItem("role")
const validEmailRegex = RegExp(/^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i);
const validMobileRegex = RegExp(/^[6-9]{1}[0-9]{9}$/);


class UpdateProfile extends Component {

  constructor(props) {
    super(props)
    this.state = {
      user: this.props.match.params.id,
      n: false,
      e: false,
      p: false,
      m: false,
      //r:false,
      name: a,
      email: b,
      mobile: c,
      interests: [],
      //role: d,
      errors: {
        name: '',
        email: '',
        mobile: '',
        //role : '',
      }
    }

    this.handleNameChange = this.handleNameChange.bind(this)
    this.handleEmailChange = this.handleEmailChange.bind(this)
    this.handleMobileChange = this.handleMobileChange.bind(this)
    //this.handleRoleChange = this.handleRoleChange.bind(this)
    //this.handleImageChange = this.handleImageChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.handleCheckboxChange = this.handleCheckboxChange.bind(this);


  }


  handleNameChange = event => {
    const { name, value } = event.target;
    let errors = this.state.errors;
    errors.name =
      value.length < 5
        ? 'Full Name must be 5 characters long!'
        : '';
    if (errors.name == '') {
      this.setState({ n: true });
    }
    this.setState({ errors, [name]: value });
  }

  handleEmailChange = event => {
    const { name, value } = event.target;
    let errors = this.state.errors;
    errors.email =
      validEmailRegex.test(value)
        ? ''
        : 'Email is not valid!';
    if (errors.email == '') {
      this.setState({ e: true });
    }
    this.setState({ errors, [name]: value });
  }

  handleMobileChange = event => {
    const { name, value } = event.target;
    let errors = this.state.errors;
    errors.mobile =
      (validMobileRegex.test(value))
        ? ''
        : 'Enter a valid phone number!';
    if (errors.mobile == '') {
      this.setState({ m: true });
    }
    this.setState({ errors, [name]: value });
  }
  handleCheckboxChange(event) {
    const target = event.target;
    var value = target.value;

    if (target.checked) {
      this.state.interests[value] = value;
    } else {
      this.state.interests.splice(value, 1);
    }
    // console.log(this.state.interests);
  }


  async handleSubmit(event) {
    event.preventDefault();

    console.log(this.state)
    var body = {
      name: this.state.name,
      email: this.state.email,
      mobile: this.state.mobile,
    }
    console.log(body);
    if (this.state.name == "") {
      alert('Please enter the name')
    }
    else if (this.state.email == "") {
      alert('Please enter the email')
    }
    else if (this.state.mobile == "") {
      alert('Please enter the phone number')
    }



    else {
      const url = "http://localhost:9000/updateProfile/" + this.state.user;
      let headers = new Headers();

      headers.append('Content-Type', 'application/json');
      headers.append('Accept', 'application/json');

      headers.append('Access-Control-Allow-origin', url);
      headers.append('Access-Control-Allow-Credentials', 'true');

      headers.append('POST', 'GET');

      await fetch(url, {
        headers: headers,
        method: 'POST',
        body: JSON.stringify(body)
      })
        .then(response => {
          if (response.ok) {
            alert("Update successful. Re-Login required.")

          }
          else {
            alert("not successful")
          }
        })

      const url1 = "http://localhost:9000/updateInterests";

      var interestsArray = [];
      Object.entries(this.state.interests).map(([key, value]) => {
        interestsArray.push(key);
      })
      console.log(this.state.user);
      var body1 = {
        uid: this.state.user,
        interests: interestsArray
      }
      let headers1 = new Headers();

      headers1.append('Content-Type', 'application/json');
      headers1.append('Accept', 'application/json');

      headers1.append('Access-Control-Allow-origin', url1);
      headers1.append('Access-Control-Allow-Credentials', 'true');

      headers1.append('POST', 'GET');
      await fetch(url1, {
        headers: headers1,
        method: 'POST',
        body: JSON.stringify(body1)
      })
        .then(response => {
          if (response.ok) {
            alert("Update successful. Re-Login required.")

          }
          else {
            alert("Not successful")
          }
        })

    }
  }


  render() {
    return (<div className="bg">
      <Nav uid={this.state.user} role={window.sessionStorage.getItem("role")} />

      <br></br><br /><br />
      <div className="auth-wrapper1">
        <div className="auth-inner">
          <form>
            <center><h3>Update Profile</h3></center>

            <div className="form-group">
              <label>Name</label>
              <input type="name"
                name="name"
                id="examplename"
                className="form-control"
                placeholder="Enter name"
                value={this.state.name}
                onChange={this.handleNameChange} required />
              <span className='error'>{this.state.errors.name}</span>
            </div>

            <div className="form-group">
              <label>Email address</label>
              <input type="email"
                name="email"
                id="exampleEmail"
                className="form-control"
                placeholder="mygmail@gmail.com"
                value={this.state.email}
                onChange={this.handleEmailChange} required />
              <span className='error'>{this.state.errors.email}</span>
            </div>

            <div className="form-group">
              <label>Mobile</label>
              <input type="phone" name="mobile" className="form-control" id="examplePhone"
                placeholder="Enter mobile number"
                value={this.state.phoneNumber}
                onChange={this.handleMobileChange} required />
              <span className='error'>{this.state.errors.mobile}</span>
            </div>
            <div class="form-row">
              <div class="form-group col-md-6">
                <label>Interests :</label><br />
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh1" value="Farm Tools" onChange={this.handleCheckboxChange} />
                  <label class="form-check-label" for="inlineCheckboxh1">Farm Tools</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh2" value="Fertilizers/Pesticides" onChange={this.handleCheckboxChange} />
                  <label class="form-check-label" for="inlineCheckboxh2">Fertilizers/Pesticides</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh3" value="Farming Techniques" onChange={this.handleCheckboxChange} />
                  <label class="form-check-label" for="inlineCheckboxh3">Farming Techniques</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh4" value="Seeds" onChange={this.handleCheckboxChange} />
                  <label class="form-check-label" for="inlineCheckboxh4">Seeds</label>
                </div>

                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh5" value="Soil" onChange={this.handleCheckboxChange} />
                  <label class="form-check-label" for="inlineCheckboxh5">Soil</label>
                </div>

                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh6" value="Climate" onChange={this.handleCheckboxChange} />
                  <label class="form-check-label" for="inlineCheckboxh6">Climate</label>
                </div>
              </div>
            </div>


            <button type="submit" className="btn btn-primary btn-block" onClick={this.handleSubmit}>Update</button>

          </form>
        </div>

      </div>
      <br /><br /><br /><br />
    </div>
    );
  }

}

export default UpdateProfile;