import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Tabs, TabsList, TabsTrigger, TabsContent } from "@radix-ui/react-tabs";

export const EnterPage = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50 py-12 px-4 sm:px-6 lg:px-8">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="space-y-1 text-center">
          <CardTitle className="text-2xl font-bold tracking-tight">
            Welcome
          </CardTitle>
          <CardDescription>
            Enter your details to access your account
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Tabs defaultValue="login" className="w-full">
            <TabsList className="grid w-full grid-cols-2 mb-8 bg-slate-100 p-1 rounded-lg">
              <TabsTrigger
                value="login"
                className="rounded-md py-2 text-sm font-medium transition-all data-[state=active]:bg-white data-[state=active]:text-slate-900 data-[state=active]:shadow-sm text-slate-500 hover:text-slate-900"
              >
                Login
              </TabsTrigger>
              <TabsTrigger
                value="register"
                className="rounded-md py-2 text-sm font-medium transition-all data-[state=active]:bg-white data-[state=active]:text-slate-900 data-[state=active]:shadow-sm text-slate-500 hover:text-slate-900"
              >
                Register
              </TabsTrigger>
            </TabsList>
            <TabsContent value="login">
              <form className="space-y-4" onSubmit={(e) => e.preventDefault()}>
                <div className="space-y-2">
                  <Input placeholder="Enter your email" type="email" />
                </div>
                <div className="space-y-2">
                  <Input placeholder="Enter your password" type="password" />
                </div>
                <Button className="w-full" type="submit">
                  Sign In
                </Button>
              </form>
            </TabsContent>
            <TabsContent value="register">
              <form className="space-y-4" onSubmit={(e) => e.preventDefault()}>
                <div className="space-y-2">
                  <Input placeholder="Enter your name" type="text" />
                </div>
                <div className="space-y-2">
                  <Input placeholder="Enter your birth date" type="date" />
                </div>
                <div className="space-y-2">
                  <Input placeholder="Enter your email" type="email" />
                </div>
                <div className="space-y-2">
                  <Input placeholder="Enter your password" type="password" />
                </div>
                <div className="space-y-2">
                  <Input placeholder="Verify your password" type="password" />
                </div>
                <Button className="w-full" type="submit">
                  Create Account
                </Button>
              </form>
            </TabsContent>
          </Tabs>
        </CardContent>
      </Card>
    </div>
  );
}