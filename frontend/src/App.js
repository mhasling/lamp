import React, {Component} from "react";
import moment from 'moment'
import ReactPlayer from 'react-player/youtube'

class App extends Component {
  state = {
    clients: [],
    url : ""
  };

  async componentDidMount() {
    const response = await fetch('/getCues');
    const body = await response.json();
    this.setState({clients: body});
  }

    onChange = (event) => {
        this.setState({url: event.target.value})
    }

  render() {
    const {clients} = this.state;
    return (
        <div className="App">
          <header className="App-header">
            <section>
                <input type="file" onChange={this.onChange}/>
              <div className='player-wrapper'>
                <ReactPlayer url={this.state.url}
                            />
              </div>
            </section>
            <aside>
                <div className="App-intro">
                  <h2>Cues</h2>
                  {clients.map(client =>
                      <div key={client.number}>
                        {client.number} ({moment(client.time).format('MM/DD/YYYY hh:mm:ss a')})
                      </div>
                  )}
                </div>
            </aside>
          </header>
        </div>
    );
  }
}

export default App;