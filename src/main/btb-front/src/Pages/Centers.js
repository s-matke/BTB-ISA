import React, { Component, useState, useEffect, useMemo } from "react";
import axios from "axios";

export default function Centers(){
    const [centers, setCenters] = useState([]);
    const [first, setFirst] = useState(true);

    const [searchFilter, setSearchFilter] = useState({
        name : "",
        address : "",
        grade : 0
    });

    useEffect(() => {
        if (first){
            loadCenters();
            setFirst(!first);
        }
    }, []);

    const loadCenters = async () => {
        const result = await axios.get('http://localhost:8084/api/center/findAll');
        setCenters(result.data);
    };

    const onInputChange = (e) => {
        setSearchFilter({ ...searchFilter, [e.target.name]: e.target.value });
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        const result = await axios.get(`http://localhost:8084/api/center/search?name=` + searchFilter.name + '&address=' + searchFilter.address + '&grade=' + searchFilter.grade);
        setCenters(result.data);
      };
    

    return (
      <div>
        <div className='py-4'>
            <form onSubmit={(e) => onSubmit(e)}>
                <input type="text" placeholder="Name" name="name" onChange={(e) => onInputChange(e)}></input>
                <input type="text" placeholder="Address" name="address" onChange={(e) => onInputChange(e)}></input>
                <input type="submit" value="Search"></input>
            </form>
            <table className="table border rounded p-4 mt-2 shadow table-striped" >
                <thead>
                    <tr className='table-dark'>
                        <th scope="col"></th>
                        <th scope="col">Name</th>
                        <th scope="col">Address</th>
                        <th scope="col">Grade</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        centers.map((center,index)=>(
                            <tr className='table-light'>
                                <td scope="row">{index+1}</td>
                                <td>{center.name}</td>
                                <td>{center.address}</td>
                                <td>{center.grade}</td>
                            </tr>
                        ))
                    }
                </tbody>
            </table>
        </div>
    </div>
    )
}