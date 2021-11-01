import React, { Component } from "react";
import moment from 'moment'
import ReactPlayer from "react-player";
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css'

class App extends Component {
  state = {
    cues: [],
    url: 'videos/HADESTOWN2019-04-13Smaller.mp4'
  };

  async componentDidMount() {
    this.loadData(); 
    setInterval(this.loadData, 1000)
    // const response = await fetch('/getCues');
    // const body = await response.json();
    // this.setState({ cues: body });
}

  loadData = () => { 
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

  render() {
    return (
      <div>
        <header>
          <h2>Lamp</h2>
        </header>
        <div className="container">
          <div className="row">
            <div className="col-lg-10">
              <ReactPlayer width="100%" height="100%" url={this.state.url} controls={true} />
            </div>
            <div className="col-lg-2">
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