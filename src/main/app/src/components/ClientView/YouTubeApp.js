import React from 'react';
import YouTubeStream from './YouTubeStream';
import YouTubeChat from './YouTubeChat';

const YouTubeApp = () => {
    const channelID = "UCMN1zTJH_O6Tq2e_o9HovVQ"; // Replace with your channel ID
    const videoID = "DQDKuNZLBv8"; // Replace with your video ID
    const domain = window.location.hostname;

    return (
        <div>
            <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', marginTop: '20px' }}>
                <YouTubeStream channel={channelID} />
                <YouTubeChat videoId={videoID} domain={domain} />
            </div>
        </div>
    );
};

export default YouTubeApp;
