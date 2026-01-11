import logo from "../assets/logo/horse_power_vehicle_logo.png";

export const Footer = () => {
  const title = "Horse Power Vehicles";

  return (
    <footer className="w-full border-t bg-slate-50 pt-16 pb-8 text-sm">
      <div className="container mx-auto px-4">
        <div className="grid grid-cols-2 gap-8 md:grid-cols-4 lg:grid-cols-6">
          <div className="col-span-2 lg:col-span-1 flex flex-col gap-4">
            <div className="flex items-center gap-2">
              <img
                src={logo}
                alt="Horse Power Vehicles logo"
                className="h-8 w-auto"
              />
              <span className="text-lg font-bold tracking-tight text-slate-900">
                {title}
              </span>
            </div>
            <p className="text-slate-500">
              Your premium destination for buying, selling, and renting
              vehicles.
            </p>
          </div>

          <div className="flex flex-col gap-4">
            <h3 className="font-semibold text-slate-900">Buy</h3>
            <ul className="space-y-2 text-slate-600">
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Bikes
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Boats
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Cars
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Planes
                </a>
              </li>
            </ul>
          </div>

          <div className="flex flex-col gap-4">
            <h3 className="font-semibold text-slate-900">Rent</h3>
            <ul className="space-y-2 text-slate-600">
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Bikes
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Boats
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Cars
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Planes
                </a>
              </li>
            </ul>
          </div>

          <div className="flex flex-col gap-4">
            <h3 className="font-semibold text-slate-900">Announce</h3>
            <ul className="space-y-2 text-slate-600">
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Bikes
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Boats
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Cars
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-slate-900 transition-colors">
                  Planes
                </a>
              </li>
            </ul>
          </div>

          <div className="flex flex-col gap-4">
            <h3 className="font-semibold text-slate-900">Admin</h3>
            <ul className="space-y-2 text-slate-600">
            <li>
                <a
                href="http://localhost:8080/q/dev-ui/welcome"
                className="hover:text-slate-900 transition-colors"
                >
                Dev UI
                </a>
            </li>
            <li>
                <a
                href="http://localhost:8080/q/dev-ui/extensions"
                className="hover:text-slate-900 transition-colors"
                >
                Dev Services
                </a>
            </li>
            <li>
                <a
                href="http://localhost:8081"
                className="hover:text-slate-900 transition-colors"
                >
                Keycloak
                </a>
            </li>
            </ul>
          </div>
          
          <div className="flex flex-col gap-4">
            <h3 className="font-semibold text-slate-900">About</h3>
            <ul className="space-y-2 text-slate-600">
            <li>
                <a
                href="#"
                className="hover:text-slate-900 transition-colors"
                >
                Project
                </a>
            </li>
            <li>
                <a
                href="#"
                className="hover:text-slate-900 transition-colors"
                >
                Wiki
                </a>
            </li>
            <li>
                <a
                href="http://localhost:8080/q/swagger-ui/"
                className="hover:text-slate-900 transition-colors"
                >
                Swagger API
                </a>
            </li>
            <li>
                <a
                href="#"
                className="hover:text-slate-900 transition-colors"
                >
                Postman Doc
                </a>
            </li>
            </ul>
          </div>
        </div>

        <div className="mt-16 border-t border-slate-200 pt-8 text-center text-slate-500">
          <p>
            &copy; {new Date().getFullYear()} Horse Power Vehicles. All rights
            reserved.
          </p>
        </div>
      </div>
    </footer>
  );
};
