import React, { Component } from "react";
import moment from 'moment'
import CreatableSelect from 'react-select/creatable';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css'
import 'font-awesome/css/font-awesome.min.css';
import Modal from 'react-modal';
import {postJson, postFile} from './Api.js'
import {Cue} from "./modals/Cue.js"
import SettingsModal from "./SettingsModal.js"

class App extends Component {
  state = {
    cues: [],
    recordings: [],
    recording : {},
    video : {},
    createCueOpen : false,
    newCue : {},
    url: 'getVideo',
    selectedCue : {},
    showSyncCues : false,
  };


  async componentDidMount() {
    this.loadData();
    this.setState({ video: document.getElementById('video-player') })
    Modal.setAppElement('body');
//    setInterval(this.loadCues, 1000)
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
      if (!!this.state.recording && !!this.state.recording.selectedRecordingId) {
        fetch('/getCues?recordingId=' + this.state.recording.selectedRecordingId).then(response => response.json())
        .then(data => {
            if (data) {
                this.setState({cues : data})
            }
        }).catch(e => {
            this.setState({cues : []})
        });
      }
  }

  onChange = (e) => {
    console.log(e.target.files);
    let files = e.target.files;
    if (files.length === 1) {
      let file = files[0];
        postFile('/addVideo?recordingId=' + this.state.recording.selectedRecordingId, file).then(data => {
            this.setState({url : "getVideo/" + this.state.recording.selectedRecordingId});
        });
//      this.setState({
//        url: URL.createObjectURL(file),
//      });
    }
  }

 handleRecordingChange = (
    newValue: OnChangeValue<ColourOption, false>,
    actionMeta: ActionMeta<ColourOption>
  ) => {
    this.setState({url : "getVideo/" + newValue.id});
    fetch('/startRecording?recordingId=' + newValue.id).then(response => response.json())
    .then(data => {
        this.setState({recording : {selectedRecordingId : newValue.id}}, () => this.loadCues());
    });
  };

  onCreateRecording = (inputValue : string) => {
    postJson('/createRecording', {name : inputValue}).then(data => {
         this.setState({recordings : data})
     });
  }

  handleCueClick = (event, cue : Cue) => {
//    this.setState({video : {...this.state.video, currentTime : cue.videoTime}});
    this.state.video.currentTime = cue.videoTime;
  }

  deleteCue = (cue) => {
    console.log(cue);
    fetch('/deleteCue?recordingId=' + this.state.recording.selectedRecordingId + "&number=" + cue.number).then(response => response.json())
    .then(data => {
        this.loadCues();
    });
  }

  syncVideoTime = (cue) => {
    fetch('/syncVideoTime?recordingId=' + this.state.recording.selectedRecordingId + "&number=" + cue.number + "&videoTime=" + this.state.video.currentTime).then(response => response.json())
    .then(data => {
        this.loadCues();
    });
  }

  addCueClicked = () => {
    this.setState({createCueOpen : true, newCue : {number : null, videoTime : this.state.video.currentTime}});
  }

  onNewCueChange = (event) => {
    this.setState({newCue : {...this.state.newCue, number : event.target.value}})
  }

  saveCue = () => {
    postJson('/createCue?recordingId=' + this.state.recording.selectedRecordingId, this.state.newCue).then(data => {
         this.setState({recordings : data})
     });
  }

  promptVideoTime = (cue) => {

  }

  render() {
    return (
      <div>
        <header>
          <h2>Lamp</h2>
        </header>
        <SettingsModal showSyncCues={this.state.showSyncCues} cue={this.state.selectedCue} />
        <Modal isOpen={this.state.createCueOpen} contentLabel="Create Cue">
            <div>
                <h2>Add Cue</h2>
                <label htmlFor="cueNumber">Cue Number</label>
                <input type="number" id="cueNumber" name="cueNumber" value={this.state.newCue.number || ''}
                        onChange={this.onNewCueChange} />
                <label htmlFor="cueLocation">Cue Video Location</label>
                <input type="number" id="cueLocation" name="cueLocation" readOnly value={this.state.newCue.videoTime}/>
                <br/>
                <button type="button" onClick={this.saveCue}>Save</button>
                <button type="button" onClick={() => { this.setState({createCueOpen : false}) }} >Close</button>
            </div>
        </Modal>
        <Modal isOpen={this.state.createCueOpen} contentLabel="Edit Recording">
            <div>
                <h2>Edit Recording</h2>
                <label htmlFor="recordingName">Cue Video Location</label>
                <br/>
                <button type="button" onClick={this.saveCue}>Save</button>
                <button type="button" onClick={() => { this.setState({createCueOpen : false}) }} >Close</button>
            </div>
        </Modal>
        <div className="mainContainer">
          <div >
            <div>
                <CreatableSelect options={this.state.recordings}
                                create
                                onCreateOption={this.onCreateRecording}
                                onChange={this.handleRecordingChange}
                                getOptionLabel={(o) => o.name}
                                getOptionValue={(o) => o.id}
                                />
              <span className="clickable" onClick={() => this.editRecording()} ><i className="fa fa-solid fa-edit" ></i></span>
//              <span className="clickable" onClick={() => this.editRecording()} ><i className="fa fa-solid fa-trash" ></i></span>
            </div>
            <div>
                <label for="video">Choose Video File</label>
                <input id="video" type="file" onChange={this.onChange} />
            </div>
          </div>
          <div className="container">
            <div className="child">
                {/*<ReactPlayer width="100%" height="100%" url={this.state.url} controls={true} /> */}
                <video id="video-player" className="videoContent" width="100%" height="100%" src={this.state.url} controls preload="none"/>
            </div>
            <div className="child cueContent">
              <h2>Cues
                  <span className="clickable" onClick={this.addCueClicked} ><i className="fa fa-solid fa-plus" ></i></span>
              </h2>
              {this.state.cues.map(cue =>
                <div key={cue.number}>
                  <a href="#" onClick={(e) => this.handleCueClick(e, cue)} > {cue.number} </a>
                  {/*({moment(cue.time).format('MM/DD/YYYY hh:mm:ss a')}) */}
                  {new Date(cue.videoTime * 1000).toISOString().substr(11, 8)}
                    <span className="clickable" onClick={() => this.deleteCue(cue)} title="Deleted Cue" ><i className="fa fa-solid fa-trash" ></i></span>
                    {!!cue.receivedTime &&
                        <span className="clickable" onClick={() => this.promptVideoTime(cue)} title="Sync Future Cues" ><i className="fa fa-solid fa-clock-o" ></i></span>
                    }
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default App;