import React from 'react'
import { Link } from 'react-router-dom';

import "../Utils/404.css";

export const E404 = () => {
    return (
        <div class="container b404">
        <div class="row">
            <div class="col-md-12">
                <div class="error-template">
                    <h1>
                        Oops!</h1>
                    <h2>
                        404 Not Found</h2>
                    <div class="error-details">
                        Sorry, an error has occured. Requested page not found!
                    </div>
                    <div class="error-actions">
                        <Link to="/home" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-home"></span>
                            Take Me Home </Link>
                    </div>
                </div>
            </div>
        </div>
    </div>
    )
}
