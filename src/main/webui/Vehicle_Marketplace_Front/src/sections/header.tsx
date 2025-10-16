import { Avatar, AvatarImage, AvatarFallback } from "@radix-ui/react-avatar"
import { Menubar, MenubarMenu, MenubarTrigger, MenubarContent, MenubarItem } from "@radix-ui/react-menubar"

const header = () => {
  //TODO: search about inset

  return (
    <>
      <img src="" alt="" />
      <h1>Rice Power Vehicles</h1>
      <Menubar>
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
        <AvatarImage src="https://github.com/shadcn.png" />
        <AvatarFallback>CN</AvatarFallback>
      </Avatar>
    </>
  )
}

export default header