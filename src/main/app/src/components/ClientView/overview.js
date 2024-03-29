import './styling/overview.css';
import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function Topic(props) {
    let topicImage;
    try {
        topicImage = require('../../../public/images/' + props.imageType);
    } catch (e) {
        topicImage = require('../../assets/fallback.png');
    }

    return (
        <Link to={`/overview/sub-overview?parentId=${props.id}&name=${props.name}`}>
            <div className="TopicContainer" style={{backgroundImage: `url(${topicImage})`}}>
                <div className="TopicTitle">
                    <p>{props.name}</p>
                </div>
            </div>
        </Link>
    );
}

export default function Overview() {
    const [topics, setTopics] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch('http://localhost:3002/api/getAllTopics', {
                    method: 'GET',
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const data = await response.json();
                console.log('Success:', data);
                setTopics(data);
            } catch (error) {
                console.error('Error:', error);
            }
        };

        fetchData();
    }, []); // Empty dependency array to ensure the effect runs once on mount

    return (
        <div>
            <div>
                <div className="overview-content">
                </div>
            </div>
            <div className="OverviewContainer">
                <div className="OverviewHeaderContainer">
                    <h2 className="OverviewHeader">Topics</h2>
                    <p className="">
                        Learn how to grow your business with our expert advice.
                    </p>
                </div>
                <div className="TopicsContainer">
                    { topics.map(item => <Topic key={item._id} id={item._id} name={item.name} imageType={item.imagePath} />)}
                </div>
            </div>
        </div>
    );
}