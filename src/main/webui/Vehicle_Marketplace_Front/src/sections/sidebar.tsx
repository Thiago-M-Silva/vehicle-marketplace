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

const items = [
  { title: "Home", url: "http://localhost:5173/", icon: Home },
  {
    title: "Dev UI",
    url: "http://localhost:8080/q/dev-ui/welcome",
    icon: Code,
  },
  {
    title: "Dev Services",
    url: "http://localhost:8080/q/dev-ui/extensions",
    icon: Code,
  },
  {
    title: "Api Docs",
    url: "http://localhost:8080/q/swagger-ui/",
    icon: FileCode,
  },
  { title: "Keycloak", url: "http://localhost:8081", icon: Search },
];

type Props = {};

export const SidebarSection = ({}: Props) => {
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
