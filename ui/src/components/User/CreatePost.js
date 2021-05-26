import React, { Component } from "react";
import '../images/bgimage.css';
import './UserHome.css'
import Nav from './nav.js';
import Select from 'react-select';
import Axios from "axios";

var body;
var urole;
var uname;
var uid;
var status;
var result;
let token = "";

class CreatePost extends Component {

    selectRef: ElementRef<*>;
    focus = () => {
        console.log(this.selectRef);
        this.selectRef.focus();
    };

    constructor(props) {
        super(props);
        this.state = {
            uid: this.props.match.params.id,
            role: this.props.match.params.role,
            title: '',
            body: '',
            postId: '',
            tags: [],
            currentFile: undefined,
            imageUrl: '',
            link: ''
        }
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleBodyChange = this.handleBodyChange.bind(this);
        this.handleTitleChange = this.handleTitleChange.bind(this);
        this.handleTagsChange = this.handleTagsChange.bind(this);
        this.handleLinkChange = this.handleLinkChange.bind(this);
        this.selectFile = this.selectFile.bind(this);

    }

    handleTitleChange(event) {
        console.log(event.target.value);
        this.setState({
            title: event.target.value
        });
    }

    handleBodyChange = event => {
        this.setState({
            body: event.target.value
        })
    }
    handleLinkChange = event => {
        this.setState({
            link: event.target.value
        })
    }

    handleTagsChange(event) {
        const target = event.target;
        var value = target.value;

        if (target.checked) {
            this.state.tags[value] = value;
        } else {
            this.state.tags.splice(value, 1);
        }
    }

    selectFile(event) {
        this.setState({
            currentFile: event.target.files[0],
        });
    }



    async handleSubmit(event) {
        event.preventDefault();
        const formData = new FormData();
        if (this.state.currentFile) {
            formData.append("file", this.state.currentFile);
            formData.append("upload_preset", "FarmersMarket");

            await Axios.post("https://api.cloudinary.com/v1_1/dpt8wpg3a/image/upload", formData).then((response => {
                // console.log(response);
                this.setState({
                    imageUrl: response.data.url
                })
            }))
        }

        if (this.state.link) {
            var url = this.state.link;

            var video_id = url.split("v=")[1].substring(0, 11)
        }
        var date = new Date();
        let options = {
            year: "numeric", month: "short",
            day: "numeric", hour: "2-digit", minute: "2-digit"
        };

        var dateTime = date.toLocaleTimeString("en-us", options);
        dateTime = dateTime.toString();

        var body = {
            uid: this.state.uid,
            role: this.state.role,
            title: this.state.title,
            body: this.state.body,
            tags: this.state.tags,
            imageUrl: this.state.imageUrl,
            link: video_id,
            dateTime: dateTime
        }
        console.log(body);
        if (this.state.title == "") {
            alert('Please enter the title')

        }
        else if (this.state.body == "") {
            alert('Please enter the content')
        }

        else {
            const url = "http://localhost:9000/createPost";
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

                .then(response => response.json())
                .then(data => {
                    this.setState({ postId: data.postId })
                });
        }
        console.log(this.state.postId)



        // console.log(this.state.currentFile);


        var tagsArray = [];
        Object.entries(this.state.tags).map(([key, value]) => {
            tagsArray.push(key);
        })
        console.log(tagsArray);
        var body1 = {
            uid: this.state.uid,
            tags: tagsArray,
            postId: this.state.postId
        }
        console.log(body1)
        const url1 = 'http://localhost:9000/registerTags';
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json');
        headers.append('Access-Control-Allow-origin', url1);
        headers.append('Access-Control-Allow-Credentials', 'true');
        headers.append('POST', 'GET');
        console.log(body1);
        await fetch(url1, {
            headers: headers,
            method: 'POST',
            body: JSON.stringify(body1)
        })
            .then(response => {
                if (response.ok) {
                    //alert("Registration successful!\nA verification link is sent to your email. You can login in after activating your account with the link.\n\nYou will be redirected to login page.");
                    alert("Post created successfully");
                    result = response.json();
                    console.log(result);
                    window.location.href = '/Home'
                }
            })
            .catch(() => console.log("Can't access " + url1 + " response. "))
    }

    render() {

        return (<div className="bg">
            <Nav uid={this.state.uid} role={this.state.role} />
            <br /><br /><br />
            <div className="auth-wrapper">
                <div className="auth-inner">

                    <form>
                        <h1>CREATE NEW POST</h1><hr />

                        <div className="form-group">
                            <label className="col-sm-2 control-label required" htmlFor="blog_post_title">Title</label>
                            <div className="col-sm-10">
                                <input type="text"
                                    id="blog_post_title"
                                    required="required"
                                    className="form-control"
                                    onChange={this.handleTitleChange}
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label className="col-sm-2 control-label required" htmlFor="blog_post_title">Body</label>
                            <div className="col-sm-10">
                                <textarea rows="5" cols="70" className="form-control" name="body" id="body"
                                    onChange={this.handleBodyChange} /><br />
                            </div>
                        </div>
                        <div className="col-8">
                            <label className="btn btn-default p-0">
                                <input type="file" accept="image/*" onChange={this.selectFile} />
                            </label>
                        </div>

                        <div className="form-group">
                            <label className="col-sm-2 control-label required" htmlFor="blog_post_link">Link</label>
                            <div className="col-sm-10">
                                <input type="text"
                                    id="blog_post_link"
                                    className="form-control"
                                    onChange={this.handleLinkChange}
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label className="col-sm-2 control-label required" htmlFor="blog_post_title">Tags</label><br></br>
                            <div className="col-sm-10">
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh1" value="Farm Tools" onChange={this.handleTagsChange} />
                                    <label class="form-check-label" for="inlineCheckboxh1">Farm Tools</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh2" value="Fertilizers/Pesticides" onChange={this.handleTagsChange} />
                                    <label class="form-check-label" for="inlineCheckboxh2">Fertilizers/Pesticides</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh3" value="Farming Techniques" onChange={this.handleTagsChange} />
                                    <label class="form-check-label" for="inlineCheckboxh3">Farming Techniques</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh4" value="Seeds" onChange={this.handleTagsChange} />
                                    <label class="form-check-label" for="inlineCheckboxh4">Seeds</label>
                                </div>

                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh5" value="Soil" onChange={this.handleTagsChange} />
                                    <label class="form-check-label" for="inlineCheckboxh5">Soil</label>
                                </div>

                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hobbies" id="inlineCheckboxh6" value="Climate" onChange={this.handleTagsChange} />
                                    <label class="form-check-label" for="inlineCheckboxh6">Climate</label>
                                </div>
                            </div>
                        </div>


                        <button type="submit" className="btn btn-primary btn-block btn-lg" onClick={this.handleSubmit}>CREATE</button>
                    </form>
                </div>
                <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
            </div>
            <br /><br /><br /><br /><br /><br />
        </div>

        );
    }
}
export default CreatePost;