import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { VerificadorPage } from './pages/VerificadorPage';
import { LoginPage } from './pages/LoginPage';
import { PanelPage } from './pages/PanelPage';

export function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<VerificadorPage />} />
        <Route path="/pasaporte/:numSerie" element={<VerificadorPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/panel" element={<PanelPage />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;