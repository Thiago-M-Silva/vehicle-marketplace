import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

export const AboutPage = () => {
  return (
    <div className="min-h-screen bg-slate-50 py-16">
      <div className="container mx-auto px-4 max-w-5xl">
        <div className="text-center mb-16">
          <h1 className="text-4xl font-extrabold tracking-tight text-slate-900 sm:text-5xl mb-6">
            About the Project
          </h1>
          <p className="text-lg text-slate-600 max-w-3xl mx-auto leading-relaxed">
            This project was created as a portfolio piece to showcase skills in
            building modern, full-stack web applications. It focuses on
            delivering a clean and intuitive user experience combined with a
            robust and scalable backend for managing vehicle announcements.
          </p>
        </div>

        <div className="grid gap-8 md:grid-cols-2 mb-16">
          <Card className="bg-white border-slate-200 shadow-sm hover:shadow-md transition-shadow duration-300">
            <CardHeader>
              <CardTitle className="text-2xl text-slate-900">
                Project Vision
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4 text-slate-600 leading-relaxed">
              <p>
                The platform demonstrates real-world concepts such as API
                design, authentication, data modeling, and frontend–backend
                integration, while keeping performance and maintainability in
                mind.
              </p>
              <p>
                More than just a demo, it reflects an approach to writing clear
                code, structuring projects thoughtfully, and solving practical
                problems.
              </p>
            </CardContent>
          </Card>

          <Card className="bg-white border-slate-200 shadow-sm hover:shadow-md transition-shadow duration-300">
            <CardHeader>
              <CardTitle className="text-2xl text-slate-900">
                Scalability & Design
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4 text-slate-600 leading-relaxed">
              <p>
                On the frontend, modern tooling is used to create a responsive
                and accessible interface, with a focus on component-based
                architecture and clean styling.
              </p>
              <p>
                The project is designed with scalability in mind, making it easy
                to extend, refactor, and deploy in real-world environments.
              </p>
            </CardContent>
          </Card>
        </div>

        <div>
          <h2 className="text-3xl font-bold tracking-tight text-slate-900 mb-10 text-center">
            Tech Stack
          </h2>
          <div className="grid gap-6 md:grid-cols-2">
            <Card className="border-t-4 border-t-blue-600 shadow-md bg-white">
              <CardHeader>
                <CardTitle className="text-xl">Backend</CardTitle>
                <CardDescription>Java & Quarkus Ecosystem</CardDescription>
              </CardHeader>
              <CardContent className="text-slate-600">
                <p className="mb-4">
                  The backend is built with <strong>Quarkus</strong>, leveraging
                  its fast startup time and developer-friendly ecosystem, along
                  with <strong>Java</strong> for strong typing and reliability.
                </p>
                <p>
                  Authentication and authorization are handled using{" "}
                  <strong>Keycloak</strong>, while integrations such as payments
                  and external services are structured to be resilient and
                  maintainable.
                </p>
              </CardContent>
            </Card>

            <Card className="border-t-4 border-t-teal-500 shadow-md bg-white">
              <CardHeader>
                <CardTitle className="text-xl">Frontend</CardTitle>
                <CardDescription>React & Modern Tooling</CardDescription>
              </CardHeader>
              <CardContent className="text-slate-600">
                <p className="mb-4">
                  Built with <strong>React</strong> and{" "}
                  <strong>TypeScript</strong> to ensure type safety and a
                  modular component structure.
                </p>
                <p>
                  Styling is handled with <strong>Tailwind CSS</strong> and{" "}
                  <strong>Shadcn UI</strong> components to provide a consistent,
                  accessible, and beautiful design system across the
                  application.
                </p>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};
