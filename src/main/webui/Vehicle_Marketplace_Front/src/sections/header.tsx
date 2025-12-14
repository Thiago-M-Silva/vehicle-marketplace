import { Avatar, AvatarImage, AvatarFallback } from "@radix-ui/react-avatar"
import { Menubar, MenubarMenu, MenubarTrigger, MenubarContent, MenubarItem } from "@radix-ui/react-menubar"
import logo from '../assets/horse_power_vehicle_logo.png'

const header = () => {
  const title: string = 'Horse Power Vehicles'

  return (
    <div className="flex flex-row items-center justify-around bg-gray-300 w-full h-20">
      <div className="flex gap-2">
        <img src={logo} alt="Horse Power Vehicles logo" className="h-12" />
        <h1 className="text-2xl font-bold">{title}</h1>
      </div>
      <Menubar className="flex justify-around gap-80">
        <MenubarMenu>
          <MenubarTrigger>Buy</MenubarTrigger>
          <MenubarContent>
            <MenubarItem> All vehicles </MenubarItem>
            <MenubarItem> Bikes </MenubarItem>
            <MenubarItem> Boats </MenubarItem>
            <MenubarItem> Cars </MenubarItem>
            <MenubarItem> Planes </MenubarItem>
          </MenubarContent>
        </MenubarMenu>
        <MenubarMenu>
          <MenubarTrigger>Sell</MenubarTrigger>
          <MenubarContent>
            <MenubarItem> Bikes </MenubarItem>
            <MenubarItem> Boats </MenubarItem>
            <MenubarItem> Cars </MenubarItem>
            <MenubarItem> Planes </MenubarItem>
          </MenubarContent>
        </MenubarMenu>
        <MenubarMenu>
          <MenubarTrigger>Rent</MenubarTrigger>
          <MenubarContent>
            <MenubarItem> Bikes </MenubarItem>
            <MenubarItem> Boats </MenubarItem>
            <MenubarItem> Cars </MenubarItem>
            <MenubarItem> Planes </MenubarItem>
          </MenubarContent>
        </MenubarMenu>
      </Menubar>
      <Avatar>
        <AvatarImage src="" />
        <AvatarFallback>Random User</AvatarFallback>
      </Avatar>
    </div>
  )
}

export default header