import { Route, Routes, useParams } from 'react-router'

function PlaceholderPage({ title }: { title: string }) {
  return (
    <main className="grid min-h-screen place-items-center bg-slate-50 p-6 text-slate-900">
      <section className="text-center">
        <p className="text-sm font-medium uppercase tracking-widest text-slate-500">
          Learning Stack
        </p>
        <h1 className="mt-3 text-3xl font-semibold">{title}</h1>
      </section>
    </main>
  )
}

function HomePage() {
  return <PlaceholderPage title="Home" />
}

function SessionPage() {
  const { sessionId } = useParams()

  return <PlaceholderPage title={`Session ${sessionId ?? ''}`} />
}

function BookmarksPage() {
  return <PlaceholderPage title="Bookmarks" />
}

function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/sessions/:sessionId" element={<SessionPage />} />
      <Route path="/bookmarks" element={<BookmarksPage />} />
    </Routes>
  )
}

export default App
