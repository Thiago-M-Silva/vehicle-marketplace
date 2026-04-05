import { useAuth } from "@/hooks/use-auth";
import { Checkout } from "@/sections/checkoutSection";
// import { isAuthenticated } from "@/services/utils";

export const PurchasePage = () => {
  const { isAuthenticated, login } = useAuth();

  if (!isAuthenticated) {
    login();
    return;
  }

  // if (!isAuthenticated()) {
  //   sessionStorage.setItem("redirectAfterLogin", window.location.pathname);
  //   window.location.href = "/enter";
  //   return;
  // }

  return <Checkout data={null} />;
};
