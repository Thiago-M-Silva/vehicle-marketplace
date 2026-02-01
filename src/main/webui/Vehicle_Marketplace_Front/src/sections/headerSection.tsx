import { Avatar, AvatarImage, AvatarFallback } from "@radix-ui/react-avatar";
import {
  Menubar,
  MenubarMenu,
  MenubarTrigger,
  MenubarContent,
  MenubarItem,
} from "@radix-ui/react-menubar";
import logo from "../assets/logo/horse_power_vehicle_logo.png";

const Header = () => {
  const title = "Horse Power Vehicles";

  const triggerStyle =
    "flex cursor-default select-none items-center rounded-md px-3 py-1.5 text-sm font-medium outline-none hover:bg-white hover:text-slate-900 focus:bg-white focus:text-slate-900 data-[state=open]:bg-white data-[state=open]:text-slate-900 transition-colors";
  const contentStyle =
    "min-w-[12rem] overflow-hidden rounded-md border bg-white p-1 shadow-md z-[100] animate-in fade-in-0 zoom-in-95";
  const itemStyle =
    "relative flex cursor-default select-none items-center rounded-sm px-2 py-1.5 text-sm outline-none hover:bg-slate-100 focus:bg-slate-100 data-[disabled]:pointer-events-none data-[disabled]:opacity-50";

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-white/95 backdrop-blur supports-[backdrop-filter]:bg-white/60">
      <div className="container mx-auto flex h-20 items-center justify-between px-4">
        <div className="flex items-center gap-3">
          <img
            src={logo}
            alt="Horse Power Vehicles logo"
            className="h-10 w-auto"
          />
          <h1 className="hidden md:block text-xl font-bold tracking-tight text-slate-900">
            {title}
          </h1>
        </div>

        <Menubar className="flex items-center gap-1 rounded-lg bg-slate-100 p-1">
          <MenubarMenu>
            <MenubarTrigger className={triggerStyle}>Buy</MenubarTrigger>
            <MenubarContent
              className={contentStyle}
              align="start"
              sideOffset={5}
            >
              <MenubarItem className={itemStyle}> <a href="/productList"> All vehicles </a> </MenubarItem>
              <MenubarItem className={itemStyle}> <a href="/productList"> Bikes </a> </MenubarItem>
              <MenubarItem className={itemStyle}> <a href="/productList"> Boats </a> </MenubarItem>
              <MenubarItem className={itemStyle}> <a href="/productList"> Cars </a> </MenubarItem>
              <MenubarItem className={itemStyle}> <a href="/productList"> Planes </a> </MenubarItem>
            </MenubarContent>
          </MenubarMenu>
          <MenubarMenu>
            <MenubarTrigger className={triggerStyle}>Sell</MenubarTrigger>
            <MenubarContent
              className={contentStyle}
              align="start"
              sideOffset={5}
            >
              <MenubarItem className={itemStyle}> <a href="/announcePage">Bikes</a> </MenubarItem>
              <MenubarItem className={itemStyle}> <a href="/announcePage">Boats</a> </MenubarItem>
              <MenubarItem className={itemStyle}> <a href="/announcePage">Cars</a> </MenubarItem>
              <MenubarItem className={itemStyle}> <a href="/announcePage">Planes</a> </MenubarItem>
            </MenubarContent>
          </MenubarMenu>
          <MenubarMenu>
            <MenubarTrigger className={triggerStyle}>Rent</MenubarTrigger>
            <MenubarContent
              className={contentStyle}
              align="start"
              sideOffset={5}
            >
              <MenubarItem className={itemStyle}> Bikes </MenubarItem>
              <MenubarItem className={itemStyle}> Boats </MenubarItem>
              <MenubarItem className={itemStyle}> Cars </MenubarItem>
              <MenubarItem className={itemStyle}> Planes </MenubarItem>
            </MenubarContent>
          </MenubarMenu>
        </Menubar>

        <Avatar className="h-10 w-10 overflow-hidden rounded-full border bg-slate-100">
          <AvatarImage src="" className="h-full w-full object-cover" />
          <AvatarFallback className="flex h-full w-full items-center justify-center bg-slate-100 text-sm font-medium text-slate-600">
            RU
          </AvatarFallback>
        </Avatar>
      </div>
    </header>
  );
};

export default Header;
