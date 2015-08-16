## Weekly Retro Template  

**Goals for last Sprint**:

Sensor catalogue and Research:

Build demo apps for all sensors, 
Modulate generated tone with sensor data.

**Goals for this Sprint**:

Multithreading the tones
Get basic background UI

**Guiding Questions** (consider before the meeting):

  *  What went well?
      We got the sensors to modulate the tone generated.
  *  What should we do differently next time?
      Make sure we have all written material with us
  *  What did we learn?
      We learned how we write/generate/play sound using audioTrack.
  *  What still puzzles us?
      How to use a multithreaded environment to manage the differect audio tracks currently playing.
 
**Team Member Analysis**:
If you did not meet your goals, how will this affect the progression of the product? What will you do differently to meet your goals this week?
We met all major development goals, we should hold true to making a sample app for all sensors since we only built demo apps for Light, Accelerometer, Magnetic North, Barometer.
  *  John
    * Goals/Responsibilities Last Week:
        * Did you meet your goals/responsibilities last week? Why or why not?
      Find a way to generate a tone. MET
    * Goals/Responsibilities This Week:
      Find new sound forms, triangle, square, saw, noise.
  *  Tasha    
    * Goals/Responsibilities Last Week:
         * Did you meet your goals/responsibilities last week? Why or why not?
      Playing sound tracks in SoundPool from raw resources. Making sure all audio stops when we press stop. MET.
    * Goals/Responsibilities This Week:
      Merge AudioTrack with SoundPool. Work on threading and multiple sounds at once.
  *  Jorge
    * Goals/Responsibilities Last Week:
        * Did you meet your goals/responsibilities last week? Why or why not?
      Build a magnetic field sensor app. MET.
    * Goals/Responsibilities This Week:
      Build background UI.
  *  George
    * Goals/Responsibilities Last Week:
       * Did you meet your goals/responsibilities last week? Why or why not?
      Manage team to meet deadlines. Troubleshoot and merge code. MET.
    * Goals/Responsibilities This Week:
      Manage team to meet deadlines. Quality check threading and new sound forms.
**Additional Discussion Topics**:

  *  Compare your work planned with what your work completed. 
      We did great this week with meeting our goals. We made a custom tone and we can change it in realtime.
  *  Is the divison of roles within your team working?
      Until now it seems like the team is happy and we are making good progress.
  *  How is communication and collaboration between team members?
      Good.
  *  Are you getting adequate support? How are you reviewing code?
      We are coding this alone for now, we will be asking John about multithreading during our weekly meeting today.
  *  How can you improve productivity and get the most work done within the next week?
      We have developed a good pace. Sound generation demands extensive researchsound forms, reverb, tremolo, flanger etc.

**Mentor Comments**:
*jrod*: So far, so good.  The team has good division of labor, experimenting with separate concepts needed to construct the final project.  I suggested 1) focusing more attention to handling multiple audio tracks and less on atypical waveforms (square, saw) and 2) an alternate way of ignoring sensor data favoring listener de/registration over threading as the latter can become tricky to manage.  We also went over when to use Handlers & Loopers.  I'm looking forward to seeing how things progress next week.
