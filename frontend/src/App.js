import React, { Component } from "react";
import moment from 'moment'
import ReactPlayer from "react-player";
import Select from 'react-select';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css'

class App extends Component {
  state = {
    cues: [],
    recordings: [],
    url: 'videos/HADESTOWN2019-04-13Smaller.mp4'
  };

  async componentDidMount() {
    this.loadData();
    setInterval(this.loadCues, 1000)
  }

  loadData = () => {
    this.loadCues();
    this.loadRecordings();
  }

  loadRecordings = () => {
    fetch('/getRecordings').then(response => response.json())
    .then(data => {
        this.setState({recordings : data})
    });
  }

  loadCues = () => {
    fetch('/getCues').then(response => response.json())
    .then(data => {
        this.setState({cues : data})
    });
  }

  onChange = (e) => {
    console.log(e.target.files);
    let files = e.target.files;
    if (files.length === 1) {
      let file = files[0];
      this.setState({
        url: URL.createObjectURL(file),
      });
    }
  }

  onCreateRecording = (inputValue : string) => {
    const requestMetadata = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({name: inputValue})
    };
    fetch('/createRecording').then(response => response.json())
    .then(data => {
        this.loadRecordings();
    });

  }

  render() {
    return (
      <div>
        <header>
          <h2>Lamp</h2>
        </header>
        <div className="container">
          {/*<div className="row">
            <Select options={this.state.recordings} create onCreateOption={onCreateRecording} />
          </div> */}
          <div className="row">
            {/*<div className="col-lg-10">
              <ReactPlayer width="100%" height="100%" url={this.state.url} controls={true} />
            </div> */}
            <div className="col-lg-4">
              <h2>Cues</h2>
              {this.state.cues.map(cue =>
                <div key={cue.number}>
                  {cue.number} ({moment(cue.time).format('MM/DD/YYYY hh:mm:ss a')})
                </div>
              )}
            </div>
          </div>
          {/* <input type="file" onChange={this.onChange} /> */}
        </div>
      </div>
    );
  }
}

export default App;