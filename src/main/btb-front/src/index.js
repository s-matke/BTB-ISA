import React from "react";
import { createRoot } from "react-dom/client";
import {
  createBrowserRouter,
  RouterProvider,
  Outlet,
} from "react-router-dom";
import StaffUpdate from "./Routes/StaffUpdate";
import Reports from "./Routes/Reports";
import Navbar from "./Components/Navbar";
import "./App.css"
import EditCenter from "./Routes/EditCenter";
import EditCenterInfo from "./Pages/EditCenterInfo";
import Signup from "./Components/Signup";
import Login from "./Components/Login";
import Home from "./Components/Home"
import Survey from "./Components/Survey"
import CreateCenter from "./Pages/CreateCenter";
import CreateStaff from "./Pages/CreateStaff";
import ShowUsers from "./Pages/ShowUsers";
import Centers from "./Pages/Centers";
import ViewProfile from "./Pages/ViewProfile";

const AppLayout = () => (
    <>
        <Navbar />
        <Outlet />
    </>
)

// const router = createBrowserRouter(createRoutesFromElements(
//     <Route element={<AppLayout />}>
//         <Route path="/" element={<Home/>} />
//         <Route path="/staff/editCenter" element={<Products/>} />
//         <Route path="/reports" element={<Reports/>} />
//     </Route>
// ))

const router = createBrowserRouter([
    {
        element: <AppLayout />,
        children: [
            {
                path: "/"
            },
            {
                path: "staff/updateProfile",
                element: <StaffUpdate />
            },
            {
                path: "staff/editCenter",
                element: <EditCenter/>,
            },
            {
                path: "reports",
                element: <Reports/>,
            },
            {
                path: "signup",
                element: <Signup/>
            },
            {
                path: "login",
                element: <Login/>
            },
            {
                path: "home",
                element: <Home />
            },
            {
                path: "survey",
                element: <Survey />
            },
            {
                path: "admin/createCenter",
                element: <CreateCenter />
            },
            {
                path: "admin/createStaff",
                element: <CreateStaff />
            },
            {
                path: "allUsers",
                element: <ShowUsers />
            },
            {
                path: "centers",
                element: <Centers />
            },
            {
                path: "viewProfile",
                element: <ViewProfile />
            }
        ]
    }
]);

createRoot(document.getElementById("root")).render(
  <RouterProvider router={router} />
);
