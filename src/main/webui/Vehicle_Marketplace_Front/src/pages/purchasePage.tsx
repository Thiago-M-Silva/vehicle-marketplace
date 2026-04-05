import { Checkout } from "@/sections/checkoutSection";
import { isAuthenticated } from "@/services/utils";

export const PurchasePage = () => {
  if (!isAuthenticated()) {
    sessionStorage.setItem("redirectAfterLogin", window.location.pathname);
    window.location.href = "/enter";
    return;
  }

  return <Checkout data={null} />;
};
