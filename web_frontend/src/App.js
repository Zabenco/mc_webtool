import React, { useRef, useEffect, useState } from 'react';

const PLAYER_HEAD_URL = 'https://i.redd.it/which-steve-head-give-you-the-most-nostalgia-v0-kdtxir23hr4d1.jpg?width=225&format=pjpg&auto=webp&s=6931f61e616175651568e49857f091494466e08d';
const INIT_SCALE = 4;
const MIN_SCALE = 1;
const MAX_SCALE = 24;
const MIN_DRAG_DIST = 3;

function App() {
  const [players, setPlayers] = useState({});
  const [spawn, setSpawn] = useState({x: 0, z: 0});
  const ws = useRef(null);
  const [selected, setSelected] = useState(null);

  const [scale, setScale] = useState(INIT_SCALE);
  const [offset, setOffset] = useState({x: 0, y: 0});
  const [drag, setDrag] = useState(null);
  const [isDragging, setIsDragging] = useState(false);
  const [centeredOnSpawn, setCenteredOnSpawn] = useState(false);

  useEffect(() => {
    ws.current = new WebSocket('ws://localhost:8080');
    ws.current.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        setPlayers(old => ({
          ...old,
          [data.name]: data,
        }));

        if (data.spawn && !centeredOnSpawn) {
          setSpawn({ x: data.spawn.x, z: data.spawn.z });
          setOffset({ x: data.spawn.x, y: data.spawn.z });
          setCenteredOnSpawn(true);
        }
      } catch (e) {}
    };
    return () => { ws.current && ws.current.close(); };
  }, [centeredOnSpawn]);


  const handleMouseDown = (e) => {
    setDrag({x: e.clientX, y: e.clientY, orig: {...offset}});
    setIsDragging(false);
  };
  const handleMouseUp = (e) => {
    setDrag(null);
    setTimeout(() => setIsDragging(false), 0);
  };
  const handleMouseMove = (e) => {
    if (!drag) return;
    const dx = (e.clientX - drag.x) / scale;
    const dy = (e.clientY - drag.y) / scale;
    if (Math.abs(dx) > MIN_DRAG_DIST || Math.abs(dy) > MIN_DRAG_DIST) setIsDragging(true);
    setOffset({
      x: drag.orig.x - dx,  
      y: drag.orig.y - dy   
    });
  };


  const zoom = (factor) => {
    setScale(prev => {
      const newScale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, prev * factor));
      return newScale;
    });
  };


  const width = 800, height = 600;
  const centerX = width/2, centerY = height/2;

  return (
    <div style={{height: "100vh", width: "100vw", display: "flex", flexDirection: "column"}}>
      <header style={{
        background: "#222",
        color: "#fff",
        padding: "16px 32px",
        fontSize: "2rem",
        fontWeight: "bold",
        letterSpacing: "2px",
        boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
      }}>
        Minecraft Web Tool
      </header>
      <div style={{flex: 1, position: "relative"}}>
        <svg
          width="100%"
          height="100%"
          viewBox={`0 0 ${width} ${height}`}
          style={{
            position: "absolute", top: 0, left: 0, width: "100%", height: "100%",
            border: "1px solid #aaa", background: "#fafafa",
            cursor: drag ? "grabbing" : "grab", userSelect: "none"
          }}
          onMouseDown={handleMouseDown}
          onMouseUp={handleMouseUp}
          onMouseLeave={handleMouseUp}
          onMouseMove={handleMouseMove}
        >
          {Object.values(players).map((p) => {
            const cx = centerX + (p.x - offset.x) * scale;
            const cy = centerY + (p.z - offset.y) * scale;
            return (
              <g key={p.name}>
                <image
                  href={PLAYER_HEAD_URL}
                  x={cx - 16}
                  y={cy - 16}
                  width={32}
                  height={32}
                  style={{cursor: "pointer", userSelect: "none"}}
                  onClick={() => !isDragging && setSelected(p)}
                  draggable={false}
                />
                <text
                  x={cx}
                  y={cy + 28}
                  fontSize={14}
                  fill="black"
                  textAnchor="middle"
                  style={{fontWeight: selected && selected.name === p.name ? 'bold' : 'normal', userSelect: "none"}}
                  pointerEvents="none"
                >{p.name}</text>
                <rect
                  x={cx - 12}
                  y={cy + 36}
                  width={24}
                  height={16}
                  fill="#ffd700"
                  rx={4}
                  cursor="pointer"
                  onClick={() => !isDragging && alert(`${p.name}: ${p.statsSummary}`)}
                />
                <text
                  x={cx}
                  y={cy + 48}
                  fontSize={11}
                  fill="black"
                  textAnchor="middle"
                  pointerEvents="none"
                  style={{userSelect: "none"}}
                >View Info</text>
              </g>
            );
          })}
        </svg>

        {/* Zoom controls */}
        <div style={{
          position: "absolute", bottom: 24, right: 24, display: "flex", flexDirection: "column",
          zIndex: 10, background: "#fff", borderRadius: 8, boxShadow: "0 2px 8px rgba(0,0,0,0.15)"
        }}>
          <button style={{fontSize: 24, padding: 8}} onClick={() => zoom(1.2)}>＋</button>
          <button style={{fontSize: 24, padding: 8}} onClick={() => zoom(1/1.2)}>－</button>
        </div>

        {selected && (
          <div style={{
            position: "absolute",
            top: 40,
            right: 40,
            background: "#eee",
            padding: 16,
            borderRadius: 10,
            boxShadow: "0 2px 12px rgba(0,0,0,0.15)",
            maxWidth: 320,
            zIndex: 2
          }}>
            <h2>{selected.name}</h2>
            <p>{selected.statsSummary}</p>
            <b>Inventory:</b>
            <ul>
              {selected.inventory.map((item, idx) => (
                <li key={idx}>{item}</li>
              ))}
            </ul>
            <b>Effects:</b> {selected.activeEffects.join(", ")}
            <br />
            <button onClick={() => setSelected(null)} style={{marginTop: 8}}>Close</button>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
