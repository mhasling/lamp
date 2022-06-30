import React, { Component } from "react";
import Modal from 'react-modal';


class SettingsModal extends Component {

    render() {
        return (
            <Modal isOpen={this.props.showSyncCues} contentLabel="Sync Cues">
                <h2 >Enter Video Time for cue {this.props.cue.number}</h2>
                <button type="button" onClick={this.saveCue}>Sync Cues</button>
                <button type="button" onClick={() => { this.setState({createCueOpen : false}) }} >Close</button>
            </Modal>
        )
    }
}

export default SettingsModal;