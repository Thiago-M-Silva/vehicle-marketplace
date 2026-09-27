import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarTrigger,
} from "@/components/ui/sidebar";
import { FileCode, Home, Code, Search } from "lucide-react";
import { KEYCLOAK_URL } from "@/config/endpoints";

const items = [
  { title: "Home", url: "/", icon: Home },
  {
    title: "Dev UI",
    url: "/q/dev-ui/welcome",
    icon: Code,
  },
  {
    title: "Dev Services",
    url: "/q/dev-ui/extensions",
    icon: Code,
  },
  {
    title: "Api Docs",
    url: "/q/swagger-ui/",
    icon: FileCode,
  },
  { title: "Keycloak", url: KEYCLOAK_URL, icon: Search },
];

type Props = {};

export const SidebarSection = ({}: Props) => {
  const user = window.sessionStorage.getItem("currentUser");
  const isAdmin = Object(user).userRole;

  console.log(isAdmin);

  if (isAdmin !== "admin") return null;

  return (
    <Sidebar collapsible="icon">
      <SidebarHeader>
        <SidebarTrigger />
      </SidebarHeader>
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Application</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {items.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton asChild>
                    <a href={item.url}>
                      <item.icon />
                      <span>{item.title}</span>
                    </a>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter />
    </Sidebar>
  );
};
