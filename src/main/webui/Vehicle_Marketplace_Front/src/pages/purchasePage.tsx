import { Checkout } from "@/sections/checkoutSection";
import { isAuthenticated } from "@/services/utils";

export const PurchasePage = () => {
    if(!isAuthenticated()){
        window.location.href = "/enter";
        return;
    }
    
    return ( 
        <Checkout data={null}/>
    );
}