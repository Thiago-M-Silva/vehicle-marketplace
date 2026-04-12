import { useAuth } from "@/hooks/use-auth";
import { LoadingSection } from "@/sections/loadingSection";
import { Navigate, Outlet, useLocation } from "react-router";

export const ProtectedRoute = () => {
  const { initialized, isAuthenticated } = useAuth();
  const location = useLocation();

  if (!initialized) {
    return <LoadingSection />;
  }

  if (!isAuthenticated) {
    const from = `${location.pathname}${location.search}${location.hash}`;

    return <Navigate to="/enter" replace state={{ from }} />;
  }

  return <Outlet />;
};
