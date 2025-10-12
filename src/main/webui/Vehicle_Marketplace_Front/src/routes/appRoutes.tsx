import Home from "@/pages/home"
import { Route, Routes } from "react-router"

export const AppRoutes: React.FC = () => {
    return (
        <Routes>
            <Route path="/" element={<Home />} />
        </Routes>
    )
}