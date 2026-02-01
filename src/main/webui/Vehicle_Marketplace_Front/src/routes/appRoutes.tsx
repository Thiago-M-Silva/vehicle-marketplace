import Home from "@/pages/homePage"
import { ProductInfo } from "@/pages/productInfoPage"
import { ProductList } from "@/pages/productListPage"
import { EnterPage } from "@/pages/enterPage"
import { Route, Routes } from "react-router"
import { AnnouncePage } from "@/pages/announcePage"
import { AboutPage } from "@/pages/aboutPage"
import { RentingPage } from "@/pages/rentingPage"
import { PurchasePage } from "@/pages/purchasePage"
import { ProfilePage } from "@/pages/profilePage"

export const AppRoutes: React.FC = () => {
    return (
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/enterPage" element={<EnterPage />} />
            <Route path="/aboutPage" element={<AboutPage />} />
            <Route path="/userRegister" element={<EnterPage />} />
            <Route path="/rentingPage" element={<RentingPage />} />
            <Route path="/profilePage" element={<ProfilePage />} />
            <Route path="/announcePage" element={<AnnouncePage />} />
            <Route path="/purchasePage" element={<PurchasePage />} />
            <Route path="/productInfo" element={<ProductInfo vehicle={ null }/>} />
            <Route path="/productList" element={<ProductList vehicle={ null }/>} />
        </Routes>
    )
}