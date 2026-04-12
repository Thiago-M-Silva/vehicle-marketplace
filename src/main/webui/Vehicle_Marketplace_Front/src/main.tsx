import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { BrowserRouter } from 'react-router'
import { SidebarProvider, SidebarTrigger } from './components/ui/sidebar.tsx'
import { SidebarSection } from './sections/sidebarSection.tsx'
import Header from './sections/headerSection.tsx'
import { Footer } from './sections/footerSection.tsx'
import { AuthProvider } from './hooks/use-auth.ts'

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <StrictMode>
      <AuthProvider>
        <SidebarProvider>
          <SidebarSection />
          <main className='w-full'>
            <SidebarTrigger className="md:hidden" />
            <Header />
            <App />
            <Footer />
          </main>
        </SidebarProvider>
      </AuthProvider>
    </StrictMode>
  </BrowserRouter>,
)
