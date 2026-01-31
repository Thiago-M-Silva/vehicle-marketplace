import Home from "@/pages/homePage"
import { ProductInfo } from "@/pages/productInfoPage"
import { ProductList } from "@/pages/productListPage"
import { EnterPage } from "@/pages/enterPage"
import { Route, Routes } from "react-router"

export const AppRoutes: React.FC = () => {
    return (
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/prodInfo" element={<ProductInfo vehicle={ null }/>} />
            <Route path="/prodList" element={<ProductList vehicle={ null }/>} />
            <Route path="/userRegister" element={<EnterPage />} />
        </Routes>
    )
}