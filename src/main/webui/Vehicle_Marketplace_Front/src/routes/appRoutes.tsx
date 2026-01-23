import Home from "@/pages/home"
import { ProductInfo } from "@/pages/productInfo"
import { Route, Routes } from "react-router"

export const AppRoutes: React.FC = () => {
    return (
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/prodInfo" element={<ProductInfo vehicle={ null }/>} />
        </Routes>
    )
}