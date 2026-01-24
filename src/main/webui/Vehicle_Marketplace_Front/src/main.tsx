import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { BrowserRouter } from 'react-router'
import { SidebarProvider, SidebarTrigger } from './components/ui/sidebar.tsx'
import { SidebarSection } from './sections/sidebarSection.tsx'
import Header from './sections/headerSection.tsx'
import { Footer } from './sections/footerSection.tsx'

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <StrictMode>
      <SidebarProvider>
        <SidebarSection />
        <main className='w-full'>
          <SidebarTrigger className="md:hidden" />
          <Header />
          <App />
          <Footer />
        </main>
      </SidebarProvider>
    </StrictMode>
  </BrowserRouter>,
)
